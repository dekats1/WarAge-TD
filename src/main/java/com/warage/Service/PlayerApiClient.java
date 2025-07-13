package com.warage.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warage.Model.Model; // Убедитесь, что этот импорт есть
import com.warage.Model.PlayerProfile; // Используем PlayerProfile из модели клиента
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.prefs.Preferences; // Для сохранения токена

public class PlayerApiClient {

    private static final String BASE_SERVER_URL = "http://localhost:8080";
    private static final String API_PLAYER_BASE_URL = BASE_SERVER_URL + "/api/v1/players";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Preferences prefs; // Для сохранения/загрузки токена

    public PlayerApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.prefs = Preferences.userNodeForPackage(PlayerApiClient.class); // Инициализация Preferences
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
     * Добавлена поддержка сохранения токенов для функции "Запомнить меня".
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param rememberMe Флаг, указывающий, нужно ли запомнить пользователя.
     * @return CompletableFuture с PlayerProfile, если логин успешен.
     */
    public CompletableFuture<PlayerProfile> loginPlayer(String username, String password, boolean rememberMe) { // ИЗМЕНЕНО: Добавлен rememberMe
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("username", username);
        requestBodyMap.put("password", password);
        // Сервер должен вернуть токены в ответе
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
                            // Предполагаем, что сервер возвращает PlayerProfile и токены в виде JSON
                            // Пример: {"playerProfile": {...}, "jwtToken": "...", "refreshToken": "..."}
                            // Вам нужно будет обновить структуру ответа сервера, чтобы она включала токены.
                            // Для простоты пока будем парсить только PlayerProfile, но в реальном приложении
                            // вы должны получить и сохранить токены.
                            Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
                            PlayerProfile playerProfile = objectMapper.convertValue(responseMap.get("playerProfile"), PlayerProfile.class);
                            String jwtToken = (String) responseMap.get("jwtToken");
                            String refreshToken = (String) responseMap.get("refreshToken");

                            // Сохраняем токены в Model (глобальное состояние)
                            Model.getInstance().setJwtToken(jwtToken);
                            Model.getInstance().setRefreshToken(refreshToken);

                            // Сохраняем токены в Preferences, если rememberMe выбран
                            if (rememberMe) {
                                prefs.put("jwtToken", jwtToken);
                                prefs.put("refreshToken", refreshToken);
                                prefs.put("lastLoggedInUsername", username); // Сохраняем имя пользователя для автологина
                            } else {
                                // Если не "Запомнить меня", очищаем старые токены
                                clearStoredTokens();
                            }
                            return playerProfile;
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse login response or tokens: " + response.body(), e);
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
                    System.err.println("Error during login request: " + e.getMessage());
                    throw new RuntimeException("Login failed: " + e.getMessage(), e);
                });
    }

    // НОВОЕ: Методы для получения сохраненных токенов
    public String getStoredJwtToken() {
        return prefs.get("jwtToken", null);
    }

    public String getStoredRefreshToken() {
        return prefs.get("refreshToken", null);
    }

    public String getRememberedUsername() {
        return prefs.get("lastLoggedInUsername", null);
    }

    // НОВОЕ: Метод для очистки сохраненных токенов
    public void clearStoredTokens() {
        prefs.remove("jwtToken");
        prefs.remove("refreshToken");
        prefs.remove("lastLoggedInUsername");
        Model.getInstance().setJwtToken(null); // Очищаем и из Model
        Model.getInstance().setRefreshToken(null);
        Model.getInstance().setPlayer(null); // Сбрасываем текущего пользователя
    }

    // НОВОЕ: Метод для получения профиля игрока с аутентификацией
    public CompletableFuture<PlayerProfile> getPlayerProfile(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("JWT token is missing."));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_PLAYER_BASE_URL + "/profile")) // Пример эндпоинта для получения профиля
                .header("Authorization", "Bearer " + jwtToken) // Добавляем JWT в заголовок
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return objectMapper.readValue(response.body(), PlayerProfile.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse PlayerProfile from response: " + response.body(), e);
                        }
                    } else if (response.statusCode() == 401) {
                        throw new RuntimeException("Unauthorized: Invalid or expired token.");
                    } else if (response.statusCode() == 403) {
                        throw new RuntimeException("Forbidden: Access denied.");
                    } else {
                        throw new RuntimeException("Server responded with status " + response.statusCode() + ": " + response.body());
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error during getPlayerProfile request: " + e.getMessage());
                    throw new RuntimeException("Failed to fetch player profile: " + e.getMessage(), e);
                });
    }

    // Здесь могут быть другие методы для взаимодействия с сервером
}