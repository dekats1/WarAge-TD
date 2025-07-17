package com.warage.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.Level;

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

public class LevelApi {

    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_LEVELS_URL = BASE_SERVER_URL + "/api/levels";
    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String LEVELS_CACHE_FILE = "levels.json";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheFilePath;

    public LevelApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheDir = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDir.resolve(LEVELS_CACHE_FILE);

        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDir + " - " + e.getMessage());
        }
    }

    public CompletableFuture<List<Level>> loadLevelsFromCache() {
        return CompletableFuture.supplyAsync(() -> {
            File cacheFile = cacheFilePath.toFile();
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    System.out.println("Loading levels from cache: " + cacheFilePath);
                    return objectMapper.readValue(cacheFile, new TypeReference<>() {
                    });
                } catch (IOException e) {
                    System.err.println("Error reading levels from cache: " + e.getMessage());
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

    public void saveLevelsToCache(List<Level> levels) {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Saving levels to cache: " + cacheFilePath);
                objectMapper.writeValue(cacheFilePath.toFile(), levels);
            } catch (IOException e) {
                System.err.println("Error writing levels to cache: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<List<Level>> getAllLevels() {
        return loadLevelsFromCache()
                .thenCompose(cachedLevels -> {
                    if (!cachedLevels.isEmpty()) {
                        System.out.println("Returned levels from cache.");
                        return CompletableFuture.completedFuture(cachedLevels);
                    } else {
                        System.out.println("Cache empty or corrupted, fetching levels from server.");
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_LEVELS_URL))
                                .GET()
                                .header("Accept", "application/json")
                                .build();

                        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenApply(body -> {
                                    try {
                                        List<Level> levels = objectMapper.readValue(body,
                                                objectMapper.getTypeFactory().constructCollectionType(List.class, Level.class));
                                        saveLevelsToCache(levels);
                                        return levels;
                                    } catch (IOException e) {
                                        System.err.println("Error parsing levels from server response: " + e.getMessage());
                                        e.printStackTrace();
                                        throw new RuntimeException("Failed to parse levels from server.", e);
                                    }
                                })
                                .exceptionally(e -> {
                                    System.err.println("Error fetching levels from server: " + e.getMessage());
                                    return List.of();
                                });
                    }
                });
    }
}
