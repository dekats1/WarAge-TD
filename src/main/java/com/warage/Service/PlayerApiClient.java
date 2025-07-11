package com.warage.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.warage.Model.PlayerProfile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PlayerApiClient {

    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_PLAYER_BASE_URL = BASE_SERVER_URL + "/api/v1/players";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PlayerApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    public CompletableFuture<PlayerProfile> registerNewPlayer(String username, String password, String email) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("username", username);
        requestBodyMap.put("password", password);
        requestBodyMap.put("email", email);
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new RuntimeException("Failed to serialize registration request", e));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .uri(URI.create(API_PLAYER_BASE_URL + "/register"))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 201) {
                        try {
                            return objectMapper.readValue(response.body(), PlayerProfile.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse PlayerProfile from registration response: " + response.body(), e);
                        }
                    } else if (response.statusCode() == 409) {
                        throw new RuntimeException("User with username or email already exists.");
                    } else {
                        throw new RuntimeException("Server responded with status " + response.statusCode() + ": " + response.body());
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error during registration request: " + e.getMessage());
                    throw new RuntimeException("Registration failed: " + e.getMessage(), e);
                });
    }

    /**
     * Отправляет запрос на логин пользователя на сервере с именем пользователя и паролем.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return CompletableFuture с PlayerProfile, если логин успешен.
     */
    public CompletableFuture<PlayerProfile> loginPlayer(String username, String password) { // <-- ДОБАВЛЕН НОВЫЙ МЕТОД
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("username", username);
        requestBodyMap.put("password", password);
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new RuntimeException("Failed to serialize login request", e));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .uri(URI.create(API_PLAYER_BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return objectMapper.readValue(response.body(), PlayerProfile.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse PlayerProfile from response: " + response.body(), e);
                        }
                    } else if (response.statusCode() == 401) { // Unauthorized (неверный пароль)
                        throw new RuntimeException("Incorrect username or password.");
                    } else if (response.statusCode() == 404) { // Not Found (пользователь не найден)
                        throw new RuntimeException("User '" + username + "' not found.");
                    } else {
                        throw new RuntimeException("Server responded with status " + response.statusCode() + ": " + response.body());
                    }
                })
                .exceptionally(e -> {
                    // Обработка общих ошибок сети или других исключений
                    System.err.println("Error during login request: " + e.getMessage());
                    throw new RuntimeException("Login failed: " + e.getMessage(), e); // Пробрасываем ошибку дальше
                });
    }

    // Здесь могут быть другие методы для взаимодействия с сервером
}