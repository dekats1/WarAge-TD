package com.warage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameController;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.warage.Model.Enemy;
import com.warage.Model.Model;
import com.warage.Model.Tower;
import com.warage.Model.Version;
import com.warage.Service.EnemyApi;
import com.warage.Service.PlayerApiClient; // Импортируйте
import com.warage.Service.TowerApi;
import com.warage.Service.VersionApi;
import com.warage.UI.Authentication.LoginUI;
import com.warage.UI.Authentication.RegistrationUI;
import com.warage.UI.CareerGameUI.CareerMapUI;
import com.warage.UI.InfinityGameUI;
import com.warage.UI.InterfaceUI.PauseMenuUI;
import com.warage.UI.MainMenuUI;
import com.warage.UI.MenuUI;
import com.warage.UI.SettingsUI;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class TowerDefenseGameApp extends GameApplication {
    private StackPane rootUI;
    private PlayerApiClient playerApiClient;

    @Override
    protected void initSettings(GameSettings settings) {
        VersionApi versionApi = new VersionApi();
        TowerApi towerApi = new TowerApi(); // Создаем экземпляр TowerApi
        settings.setWidth(1600);
        settings.setHeight(800);
        settings.setTitle("WarAge");
        Version currentVersion = null;
        EnemyApi enemyApi = new EnemyApi();
        List<Tower> allTowers; // Инициализируем пустым списком
        List<Enemy> allEnemies = Collections.emptyList();
        try {
            // Получение версии
            Optional<Version> versionOptional = versionApi.getLatestVersion().get(5, TimeUnit.SECONDS);
            if (versionOptional.isPresent()) {
                currentVersion = versionOptional.get();
                Model.getInstance().setVersion(currentVersion.getVersion());
            } else {
                System.err.println("Не удалось получить последнюю версию с API. Используется версия по умолчанию.");
                Model.getInstance().setVersion("1.0.0"); // Запасной вариант
            }

            // Получение башен
            // Здесь мы вызываем .get() на CompletableFuture, чтобы дождаться результата
            allTowers = towerApi.getAllTowers().get(5, TimeUnit.SECONDS);
            allEnemies = enemyApi.getAllEnemies().get(5, TimeUnit.SECONDS);
            if (allTowers.isEmpty()) {
                System.out.println("Список башен пуст или не получен.");
            } else {
                System.out.println("Загружено " + allTowers.size() + " башен.");
                // Здесь вы можете сохранить полученные башни в вашей Model,
                // если это требуется для глобального доступа
                // Model.getInstance().setTowers(allTowers); // Пример: если у вас есть сеттер для башен в Model
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) { // Добавляем TimeoutException
            System.err.println("Ошибка при получении данных: " + e.getMessage());
            e.printStackTrace(); // Вывод стека для детальной информации
            Model.getInstance().setVersion("1.0.0"); // Запасной вариант для версии
            // allTowers останется пустым списком
        }
        settings.setFullScreenFromStart(true);
        settings.setFullScreenAllowed(true);
    }

    @Override
    protected void initInput() {

    }


    @Override
    protected void initUI() {
        rootUI = new StackPane();
        addUINode(rootUI);
        playerApiClient = new PlayerApiClient();

        String storedJwt = playerApiClient.getStoredJwtToken();

        if (storedJwt != null && !storedJwt.isEmpty()) {
            playerApiClient.getPlayerProfile(storedJwt)
                    .thenAccept(playerProfile -> Platform.runLater(() -> {
                        if (playerProfile != null) {
                            Model.getInstance().setPlayer(playerProfile);
                            System.out.println("Автоматический вход для пользователя: " + playerProfile.getUsername());
                            showMainMenuUI(); // Успешный автологин, показываем главное меню
                        } else {
                            System.out.println("JWT недействителен, требуется повторный вход.");
                            playerApiClient.clearStoredTokens(); // Очищаем недействительные токены
                            showLoginUI();
                        }
                    }))
                    .exceptionally(e -> {
                        System.err.println("Ошибка при автологине с JWT: " + e.getMessage());
                        playerApiClient.clearStoredTokens(); // Очищаем токены, которые вызвали ошибку
                        Platform.runLater(this::showLoginUI);
                        return null;
                    });
        } else {
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
        },this::showCareerMapUI,this::showInfinityGameUI);
        setUI(gameUI.getRoot());
    }

    private void showCareerMapUI(){
        var careerMapUI = new CareerMapUI();
        setUI(careerMapUI.getRoot());
    }

    private void showInfinityGameUI(){
        GameController gameController = getGameController();
        var infinityGame = new InfinityGameUI(this::showMenuUI, gameController);
        setUI(infinityGame.getRoot());
    }

    public static void main(String[] args) {
        launch(args);
    }
}