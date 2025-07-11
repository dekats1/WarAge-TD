package com.warage.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.Achievement;
import com.warage.Model.PlayerAchievement;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerAchievementsApi {
    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_PLAYER_BASE_URL = BASE_SERVER_URL + "/api/player-achievements";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PlayerAchievementsApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    // Для получения всех ачивок юзера
    public CompletableFuture<List<PlayerAchievement>> getAllPlayerAchievements() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_PLAYER_BASE_URL))
                .GET()
                .header("Accept", "application/json")
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return objectMapper.readValue(body,
                                objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerAchievement.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return List.of();
                    }
                });
    }
}
