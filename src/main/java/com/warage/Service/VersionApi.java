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
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class VersionApi {
    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_VERSION_LATEST_URL = BASE_SERVER_URL + "/api/version/latest";

    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String VERSION_CACHE_FILE = "latest_version.json";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheDirPath;
    private final Path cacheFilePath;

    public VersionApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        this.cacheDirPath = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDirPath.resolve(VERSION_CACHE_FILE);

        try {
            Files.createDirectories(cacheDirPath);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDirPath + " - " + e.getMessage());
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
                        System.err.println("Error deleting corrupted version cache file: " + deleteEx.getMessage());
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

    private CompletableFuture<Void> clearCacheDirectory() {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Clearing entire cache directory: " + cacheDirPath);
            if (Files.exists(cacheDirPath)) {
                try (Stream<Path> walk = Files.walk(cacheDirPath)) {
                    walk.sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    System.err.println("Error clearing cache directory: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                Files.createDirectories(cacheDirPath);
            } catch (IOException e) {
                System.err.println("Failed to recreate cache directory after clearing: " + cacheDirPath + " - " + e.getMessage());
            }
        });
    }

    public CompletableFuture<Optional<Version>> getLatestVersion() {
        System.out.println("Fetching latest version from server...");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_VERSION_LATEST_URL))
                .GET()
                .header("Accept", "application/json")
                .build();

        CompletableFuture<Optional<Version>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            Version serverVersion = objectMapper.readValue(response.body(), Version.class);
                            System.out.println("Server version received: " + serverVersion.getVersion());

                            return loadVersionFromCache().thenCompose(cachedVersionOpt -> {
                                if (cachedVersionOpt.isPresent()) {
                                    Version cachedVersion = cachedVersionOpt.get();
                                    boolean versionsMatch = serverVersion.getVersion().equals(cachedVersion.getVersion())
                                            && serverVersion.getDataVersion().equals(cachedVersion.getDataVersion());

                                    if (!versionsMatch) {
                                        return clearCacheDirectory()
                                                .thenCompose(v -> saveVersionToCache(serverVersion))
                                                .thenApply(v -> Optional.of(serverVersion));
                                    }
                                    return CompletableFuture.completedFuture(Optional.of(serverVersion));
                                } else {
                                    return clearCacheDirectory()
                                            .thenCompose(v -> saveVersionToCache(serverVersion))
                                            .thenApply(v -> Optional.of(serverVersion));
                                }
                            });
                        } catch (IOException e) {
                            System.err.println("Error parsing version: " + e.getMessage());
                            return CompletableFuture.completedFuture(Optional.empty());
                        }
                    } else {
                        System.err.println("Server error: " + response.statusCode());
                        return CompletableFuture.completedFuture(Optional.empty());
                    }
                });

        return future.exceptionally(ex -> {
            System.err.println("Request failed: " + ex.getMessage());
            return Optional.empty();
        });
    }
}