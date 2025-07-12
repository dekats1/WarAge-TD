package com.warage.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.Version;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class VersionApi {
    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_VERSION_LATEST_URL = BASE_SERVER_URL + "/api/version/latest";

    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String VERSION_CACHE_FILE = "latest_version.json";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheFilePath;

    public VersionApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheDir = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDir.resolve(VERSION_CACHE_FILE);

        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDir + " - " + e.getMessage());
        }
    }


    public CompletableFuture<Optional<Version>> loadVersionFromCache() {
        return CompletableFuture.supplyAsync(() -> {
            File cacheFile = cacheFilePath.toFile();
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    System.out.println("Loading version from cache: " + cacheFilePath);
                    return Optional.ofNullable(objectMapper.readValue(cacheFile, Version.class));
                } catch (IOException e) {
                    System.err.println("Error reading version from cache: " + e.getMessage());
                    try {
                        Files.deleteIfExists(cacheFilePath);
                    } catch (IOException deleteEx) {
                        System.err.println("Error deleting corrupted cache file: " + deleteEx.getMessage());
                    }
                }
            }
            return Optional.empty();
        });
    }

    public CompletableFuture<Void> saveVersionToCache(Version version) {
        return CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Saving version to cache: " + cacheFilePath);
                objectMapper.writeValue(cacheFilePath.toFile(), version);
            } catch (IOException e) {
                System.err.println("Error writing version to cache: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<Optional<Version>> getLatestVersion() {
        return loadVersionFromCache().thenCompose(cachedVersionOptional -> {
            if (cachedVersionOptional.isPresent()) {
                System.out.println("Returned version from cache.");
                return CompletableFuture.completedFuture(cachedVersionOptional);
            } else {
                System.out.println("Cache empty or corrupted, fetching from server.");
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_VERSION_LATEST_URL)).GET().header("Accept", "application/json").build();

                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            Version serverVersion = objectMapper.readValue(response.body(), Version.class);
                            saveVersionToCache(serverVersion);
                            return Optional.of(serverVersion);
                        } catch (IOException e) {
                            System.err.println("Error parsing version from server response: " + e.getMessage());
                            e.printStackTrace();
                            // Явно указываем тип для Optional.empty()
                            return Optional.<Version>empty();
                        }
                    } else if (response.statusCode() == 404) {
                        System.err.println("Server returned 404 (Not Found) for latest version.");
                        // Явно указываем тип для Optional.empty()
                        return Optional.<Version>empty();
                    } else {
                        System.err.println("Server responded with status " + response.statusCode() + ": " + response.body());
                        // Явно указываем тип для Optional.empty()
                        return Optional.<Version>empty();
                    }
                }).exceptionally(e -> {
                    System.err.println("Error fetching version from server: " + e.getMessage());
                    // Явно указываем тип для Optional.empty()
                    return Optional.<Version>empty();
                });
            }
        });
    }
}