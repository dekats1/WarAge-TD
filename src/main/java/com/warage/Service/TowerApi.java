package com.warage.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.Tower; // Импортируем модель Tower

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

public class TowerApi {
    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_PLAYER_TOWERS_URL = BASE_SERVER_URL + "/api/towers"; // Эндпоинт для башен
    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String TOWERS_CACHE_FILE = "player_towers.json"; // Имя файла кэша для башен

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheFilePath;

    public TowerApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheDir = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDir.resolve(TOWERS_CACHE_FILE);

        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDir + " - " + e.getMessage());
        }
    }

    /**
     * Загружает данные о башнях игрока из кэша.
     *
     * @return CompletableFuture, содержащий список объектов Tower.
     */
    public CompletableFuture<List<Tower>> loadTowersFromCache() {
        return CompletableFuture.supplyAsync(() -> {
            File cacheFile = cacheFilePath.toFile();
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    System.out.println("Loading towers from cache: " + cacheFilePath);
                    return objectMapper.readValue(cacheFile, new TypeReference<>() {
                    });
                } catch (IOException e) {
                    System.err.println("Error reading towers from cache: " + e.getMessage());
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
     * Сохраняет данные о башнях игрока в кэш.
     *
     * @param towers Список объектов Tower для сохранения.
     */
    public void saveTowersToCache(List<Tower> towers) {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Saving towers to cache: " + cacheFilePath);
                objectMapper.writeValue(cacheFilePath.toFile(), towers);
            } catch (IOException e) {
                System.err.println("Error writing towers to cache: " + e.getMessage());
            }
        });
    }

    /**
     * Получает все башни игрока. Сначала пытается загрузить из кэша,
     * в случае неудачи или пустого кэша - с сервера.
     *
     * @return CompletableFuture, содержащий список объектов Tower.
     */
    public CompletableFuture<List<Tower>> getAllTowers() {
        // Сначала пытаемся загрузить из кэша
        return loadTowersFromCache()
                .thenCompose(cachedTowers -> {
                    if (!cachedTowers.isEmpty()) {
                        System.out.println("Returned towers from cache.");
                        return CompletableFuture.completedFuture(cachedTowers);
                    } else {
                        // Если кэш пуст или ошибка, загружаем с сервера
                        System.out.println("Cache empty or corrupted, fetching from server.");
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_PLAYER_TOWERS_URL))
                                .GET()
                                .header("Accept", "application/json")
                                .build();

                        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenApply(body -> {
                                    try {
                                        List<Tower> towers = objectMapper.readValue(body,
                                                objectMapper.getTypeFactory().constructCollectionType(List.class, Tower.class));
                                        // Сохраняем полученные с сервера данные в кэш
                                        saveTowersToCache(towers);
                                        return towers;
                                    } catch (IOException e) {
                                        System.err.println("Error parsing towers from server response: " + e.getMessage());
                                        e.printStackTrace(); // Для более детальной информации об ошибке
                                        throw new RuntimeException("Failed to parse player towers from server.", e);
                                    }
                                })
                                .exceptionally(e -> {
                                    System.err.println("Error fetching towers from server: " + e.getMessage());
                                    // Можно вернуть пустой список или бросить исключение
                                    return List.of();
                                });
                    }
                });
    }
}