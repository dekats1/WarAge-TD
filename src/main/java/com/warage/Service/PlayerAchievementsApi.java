package com.warage.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.PlayerAchievement;

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

public class PlayerAchievementsApi {
    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_PLAYER_BASE_URL = BASE_SERVER_URL + "/api/player-achievements";
    private static final String CACHE_DIR_NAME = "warage_cache";
    private static final String ACHIEVEMENTS_CACHE_FILE = "player_achievements.json";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Path cacheFilePath;

    public PlayerAchievementsApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheDir = userHome.resolve("." + CACHE_DIR_NAME);
        this.cacheFilePath = cacheDir.resolve(ACHIEVEMENTS_CACHE_FILE);

        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + cacheDir + " - " + e.getMessage());
        }
    }


    public CompletableFuture<List<PlayerAchievement>> loadAchievementsFromCache() {
        return CompletableFuture.supplyAsync(() -> {
            File cacheFile = cacheFilePath.toFile();
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    System.out.println("Loading achievements from cache: " + cacheFilePath);
                    return objectMapper.readValue(cacheFile, new TypeReference<>() {
                    });
                } catch (IOException e) {
                    System.err.println("Error reading achievements from cache: " + e.getMessage());
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
     * Сохранить достижения в кэш.
     *
     * @param achievements Список достижений для сохранения.
     */
    public void saveAchievementsToCache(List<PlayerAchievement> achievements) {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Saving achievements to cache: " + cacheFilePath);
                objectMapper.writeValue(cacheFilePath.toFile(), achievements);
            } catch (IOException e) {
                System.err.println("Error writing achievements to cache: " + e.getMessage());
            }
        });
    }

    // Для получения всех ачивок юзера (изменен, чтобы использовать кэш)
    public CompletableFuture<List<PlayerAchievement>> getAllPlayerAchievements() {
        // Сначала пытаемся загрузить из кэша
        return loadAchievementsFromCache()
                .thenCompose(cachedAchievements -> {
                    if (!cachedAchievements.isEmpty()) {
                        System.out.println("Returned achievements from cache.");
                        return CompletableFuture.completedFuture(cachedAchievements);
                    } else {
                        // Если кэш пуст или ошибка, загружаем с сервера
                        System.out.println("Cache empty or corrupted, fetching from server.");
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_PLAYER_BASE_URL))
                                .GET()
                                .header("Accept", "application/json")
                                .build();

                        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenApply(body -> {
                                    try {
                                        List<PlayerAchievement> achievements = objectMapper.readValue(body,
                                                objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerAchievement.class));
                                        // Сохраняем полученные с сервера данные в кэш
                                        saveAchievementsToCache(achievements);
                                        return achievements;
                                    } catch (IOException e) {
                                        System.err.println("Error parsing achievements from server response: " + e.getMessage());
                                        e.printStackTrace(); // Для более детальной информации об ошибке
                                        throw new RuntimeException("Failed to parse player achievements from server.", e);
                                    }
                                })
                                .exceptionally(e -> {
                                    System.err.println("Error fetching achievements from server: " + e.getMessage());
                                    // Можно вернуть пустой список или бросить исключение
                                    return List.of();
                                });
                    }
                });
    }
}
