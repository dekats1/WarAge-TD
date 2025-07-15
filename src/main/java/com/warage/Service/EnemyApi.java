package com.warage.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.Enemy; // Импортируем модель Enemy

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

public class EnemyApi {
    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_ENEMIES_URL = BASE_SERVER_URL + "/api/enemies"; // Эндпоинт для врагов
    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String ENEMIES_CACHE_FILE = "enemies.json"; // Имя файла кэша для врагов

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheFilePath;

    public EnemyApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheDir = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDir.resolve(ENEMIES_CACHE_FILE);

        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDir + " - " + e.getMessage());
        }
    }

    /**
     * Загружает данные о врагах из кэша.
     *
     * @return CompletableFuture, содержащий список объектов Enemy.
     */
    public CompletableFuture<List<Enemy>> loadEnemiesFromCache() {
        return CompletableFuture.supplyAsync(() -> {
            File cacheFile = cacheFilePath.toFile();
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    System.out.println("Loading enemies from cache: " + cacheFilePath);
                    return objectMapper.readValue(cacheFile, new TypeReference<>() {
                    });
                } catch (IOException e) {
                    System.err.println("Error reading enemies from cache: " + e.getMessage());
                    // Удаляем поврежденный файл кэша, чтобы следующая попытка была с сервера
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

    /**
     * Сохраняет данные о врагах в кэш.
     *
     * @param enemies Список объектов Enemy для сохранения.
     */
    public void saveEnemiesToCache(List<Enemy> enemies) {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Saving enemies to cache: " + cacheFilePath);
                objectMapper.writeValue(cacheFilePath.toFile(), enemies);
            } catch (IOException e) {
                System.err.println("Error writing enemies to cache: " + e.getMessage());
            }
        });
    }

    /**
     * Получает всех врагов. Сначала пытается загрузить из кэша,
     * в случае неудачи или пустого кэша - с сервера.
     *
     * @return CompletableFuture, содержащий список объектов Enemy.
     */
    public CompletableFuture<List<Enemy>> getAllEnemies() {
        return loadEnemiesFromCache()
                .thenCompose(cachedEnemies -> {
                    if (!cachedEnemies.isEmpty()) {
                        System.out.println("Returned enemies from cache.");
                        return CompletableFuture.completedFuture(cachedEnemies);
                    } else {
                        System.out.println("Cache empty or corrupted, fetching enemies from server.");
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_ENEMIES_URL))
                                .GET()
                                .header("Accept", "application/json")
                                .build();

                        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenApply(body -> {
                                    try {
                                        List<Enemy> enemies = objectMapper.readValue(body,
                                                objectMapper.getTypeFactory().constructCollectionType(List.class, Enemy.class));
                                        saveEnemiesToCache(enemies);
                                        return enemies;
                                    } catch (IOException e) {
                                        System.err.println("Error parsing enemies from server response: " + e.getMessage());
                                        e.printStackTrace();
                                        throw new RuntimeException("Failed to parse enemies from server.", e);
                                    }
                                })
                                .exceptionally(e -> {
                                    System.err.println("Error fetching enemies from server: " + e.getMessage());
                                    return List.of();
                                });
                    }
                });
    }
}