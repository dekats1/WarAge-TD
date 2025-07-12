package com.warage;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.warage.Model.Model;
import com.warage.Model.Version;
import com.warage.Service.VersionApi;
import com.warage.UI.Authentication.LoginUI;
import com.warage.UI.Authentication.RegistrationUI;
import com.warage.UI.CareerGameUI.CareerMapUI;
import com.warage.UI.MainMenuUI;
import com.warage.UI.MenuUI;
import com.warage.UI.SettingsUI;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;

public class TowerDefenseGameApp extends GameApplication {
    private StackPane rootUI;

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
                settings.setVersion(currentVersion.getVersion());
                System.out.println("Application initialized with version: " + currentVersion.getVersion());
            } else {
                System.err.println("Failed to retrieve application version (empty Optional). Using default.");
                settings.setVersion("Unknown Version"); // Установим версию по умолчанию
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching latest version, using default: " + e.getMessage());
            settings.setVersion("Error Version"); // Установим версию по умолчанию при ошибке
            e.printStackTrace(); // Для отладки
        } catch (java.util.concurrent.TimeoutException e) {
            System.err.println("Timeout fetching latest version, using default: " + e.getMessage());
            settings.setVersion("Timeout Version"); // Установим версию по умолчанию при таймауте
            e.printStackTrace();
        }
        Model.getInstance().setVersion(settings.getVersion());
        settings.setFullScreenFromStart(true);
        settings.setFullScreenAllowed(true);
    }

    @Override
    protected void initGame() {
        // Здесь будет логика инициализации вашего игрового мира
        // Например, загрузка карты, размещение первых башен, спавн врагов
        //FXGL.getGameWorld().addEntityFactory(new MyTowerFactory()); // Ваша фабрика сущностей
        // ... аиапра
    }

    @Override
    protected void initPhysics() {
        // Здесь будут обработчики коллизий и физика (если нужны)
    }

    @Override
    protected void initInput() {
        // Здесь будет обработка пользовательского ввода (клавиатура, мышь)
    }

    @Override
    protected void initUI() {
        rootUI = new StackPane();
        addUINode(rootUI);

        showMenuUI();
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

    private void showCareerMapUI() {
        var mapUI = new CareerMapUI();
        setUI(mapUI.getRoot());
    }


    // Если вы хотите запускать игру из отдельного main-метода (для тестирования)
    public static void main(String[] args) {
        launch(args);
    }
}