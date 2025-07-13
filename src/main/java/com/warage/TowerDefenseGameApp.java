package com.warage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.warage.Model.Model;
import com.warage.Model.Version;
import com.warage.Service.PlayerApiClient; // Импортируйте
import com.warage.Service.VersionApi;
import com.warage.UI.Authentication.LoginUI;
import com.warage.UI.Authentication.RegistrationUI;
import com.warage.UI.CareerGameUI.CareerMapUI;
import com.warage.UI.MainMenuUI;
import com.warage.UI.MenuUI;
import com.warage.UI.SettingsUI;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture; // Импорт для CompletableFuture

import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;

public class TowerDefenseGameApp extends GameApplication {
    private StackPane rootUI;
    private PlayerApiClient playerApiClient; // Добавьте это

    @Override
    protected void initSettings(GameSettings settings) {
        VersionApi versionApi = new VersionApi();
        settings.setWidth(1600);
        settings.setHeight(800);
        settings.setTitle("WarAge");
        Version currentVersion = null;
        try {
            Optional<?> versionOptional = versionApi.getLatestVersion().get(5, TimeUnit.SECONDS);
            if (versionOptional.isPresent()) {
                currentVersion = (Version) versionOptional.get();
                Model.getInstance().setVersion(currentVersion.getVersion());
            } else {
                System.err.println("Failed to get latest version from API. Using default.");
                Model.getInstance().setVersion("1.0.0"); // Fallback
            }
        } catch (InterruptedException | ExecutionException | java.util.concurrent.TimeoutException e) {
            System.err.println("Error fetching version: " + e.getMessage());
            Model.getInstance().setVersion("1.0.0"); // Fallback
        }
    }

    @Override
    protected void initInput() {
        // Здесь будет обработка пользовательского ввода (клавиатура, мышь)
    }

    @Override
    protected void initUI() {
        rootUI = new StackPane();
        addUINode(rootUI);
        playerApiClient = new PlayerApiClient(); // Инициализация PlayerApiClient

        // Логика автоматического входа
        String storedJwt = playerApiClient.getStoredJwtToken();
        String storedRefresh = playerApiClient.getStoredRefreshToken();
        String rememberedUsername = playerApiClient.getRememberedUsername();

        if (storedJwt != null && !storedJwt.isEmpty()) {
            // Попробуйте использовать JWT для получения профиля
            playerApiClient.getPlayerProfile(storedJwt)
                    .thenAccept(playerProfile -> Platform.runLater(() -> {
                        if (playerProfile != null) {
                            Model.getInstance().setPlayer(playerProfile);
                            System.out.println("Автоматический вход для пользователя: " + playerProfile.getUsername());
                            showMainMenuUI(); // Успешный автологин, показываем главное меню
                        } else {
                            // JWT недействителен или устарел, переходим к экрану входа
                            System.out.println("JWT недействителен, требуется повторный вход.");
                            playerApiClient.clearStoredTokens(); // Очищаем недействительные токены
                            showLoginUI();
                        }
                    }))
                    .exceptionally(e -> {
                        // Ошибка при проверке JWT (например, сеть, серверная ошибка),
                        // или JWT просрочен и сервер вернул 401/403.
                        // Здесь можно было бы попытаться обновить токен с помощью refreshToken.
                        // Для простоты, пока просто переходим к экрану входа.
                        System.err.println("Ошибка при автологине с JWT: " + e.getMessage());
                        playerApiClient.clearStoredTokens(); // Очищаем токены, которые вызвали ошибку
                        Platform.runLater(this::showLoginUI);
                        return null;
                    });
        } else {
            // Нет сохраненных токенов, показываем UI входа
            showLoginUI();
        }
    }

    private void setUI(Node Ui){
        rootUI.getChildren().setAll(Ui);
    }

    private void showLoginUI(){
        var loginUI = new LoginUI(this::showMainMenuUI,this::showRegistrationUI);
        setUI(loginUI.getRoot());
    }

    private void showRegistrationUI() {
        var registrationUI = new RegistrationUI(this::showLoginUI, this::showMainMenuUI);
        setUI(registrationUI.getRoot());
    }

    private void showMainMenuUI() {
        var menuUI = new MainMenuUI(this::showMenuUI, this::showSettingsUI,this::showLoginUI);
        setUI(menuUI.getRoot());
    }

    private void showSettingsUI() {
        var settingsUI = new SettingsUI(this::showMainMenuUI);
        setUI(settingsUI.getRoot());
    }

    private void showMenuUI() {
        var gameUI = new MenuUI(achievemntNode->{
            rootUI.getChildren().add(achievemntNode);
        },this::showCareerMapUI);
        setUI(gameUI.getRoot());
    }

    private void showCareerMapUI(){
        var careerMapUI = new CareerMapUI();
        setUI(careerMapUI.getRoot());
    }

    public static void main(String[] args) {
        launch(args);
    }
}