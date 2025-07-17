package com.warage.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.TowerUpgrade;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UpgradeTowerApi {

    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_TOWER_UPGRADES_URL = BASE_SERVER_URL + "/api/tower-upgrades";
    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String TOWER_UPGRADES_CACHE_FILE = "tower-upgrades.json";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheFilePath;

    public UpgradeTowerApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheDir = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDir.resolve(TOWER_UPGRADES_CACHE_FILE);

        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDir + " - " + e.getMessage());
        }
    }

    public CompletableFuture<List<TowerUpgrade>> loadTowerUpgradesFromCache() {
        return CompletableFuture.supplyAsync(() -> {
            File cacheFile = cacheFilePath.toFile();
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    System.out.println("Loading Tower Upgrade from cache: " + cacheFilePath);
                    return objectMapper.readValue(cacheFile, new TypeReference<>() {
                    });
                } catch (IOException e) {
                    System.err.println("Error reading TowerUpgrade from cache: " + e.getMessage());
                    try {
                        Files.deleteIfExists(cacheFilePath);
                    } catch (IOException deleteEx) {
                        System.err.println("Error deleting corrupted cache file: " + deleteEx.getMessage());
                    }
                }
            }
            return Collections.emptyList();
        });
    }

    public void saveTowerUpgradesToCache(List<TowerUpgrade> TowerUpgrade) {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Saving Tower Upgrade to cache: " + cacheFilePath);
                objectMapper.writeValue(cacheFilePath.toFile(), TowerUpgrade);
            } catch (IOException e) {
                System.err.println("Error writing Tower Upgrade to cache: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<List<TowerUpgrade>> getAllTowerUpgrades() {
        return loadTowerUpgradesFromCache()
                .thenCompose(cachedTowerUpgradeS -> {
                    if (!cachedTowerUpgradeS.isEmpty()) {
                        System.out.println("Returned Tower Upgrade from cache.");
                        return CompletableFuture.completedFuture(cachedTowerUpgradeS);
                    } else {
                        System.out.println("Cache empty or corrupted, fetching Tower Upgrade from server.");
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_TOWER_UPGRADES_URL))
                                .GET()
                                .header("Accept", "application/json")
                                .build();

                        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenApply(body -> {
                                    try {
                                        List<TowerUpgrade> towerUpgrade = objectMapper.readValue(body,
                                                objectMapper.getTypeFactory().constructCollectionType(List.class, TowerUpgrade.class));
                                        saveTowerUpgradesToCache(towerUpgrade);
                                        return towerUpgrade;
                                    } catch (IOException e) {
                                        System.err.println("Error parsing Tower Upgrade from server response: " + e.getMessage());
                                        e.printStackTrace();
                                        throw new RuntimeException("Failed to parse Tower Upgrade from server.", e);
                                    }
                                })
                                .exceptionally(e -> {
                                    System.err.println("Error fetching Tower Upgrade from server: " + e.getMessage());
                                    return List.of();
                                });
                    }
                });
    }
}

