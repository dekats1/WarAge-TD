package com.warage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.warage.UI.Authentication.LoginUI;
import com.warage.UI.Authentication.RegistrationUI;
import com.warage.UI.MainMenuUI;
import com.warage.UI.MenuUI;
import com.warage.UI.SettingsUI;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;

public class TowerDefenseGameApp extends GameApplication {
    private StackPane rootUI;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1600);
        settings.setHeight(800);
        settings.setTitle("WarAge");
        settings.setVersion("0.1");
        settings.setFullScreenFromStart(true);
        settings.setFullScreenAllowed(true);
    }

    @Override
    protected void initGame() {
        // Здесь будет логика инициализации вашего игрового мира
        // Например, загрузка карты, размещение первых башен, спавн врагов
        //FXGL.getGameWorld().addEntityFactory(new MyTowerFactory()); // Ваша фабрика сущностей
        // ...
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

        showLoginUI();
    }

    private void setUI(Node Ui){
        rootUI.getChildren().setAll(Ui);
    }

    private void showLoginUI(){
        var loginUI = new LoginUI(this::showMainMenuUI,this::showRegistrationUI);
        setUI(loginUI.getRoot());
    }

    private void showRegistrationUI() {
        var registrationUI = new RegistrationUI(this::showLoginUI);
        setUI(registrationUI.getRoot());
    }

    private void showMainMenuUI() {
        var menuUI = new MainMenuUI(this::showGameUI, this::showSettingsUI,this::showLoginUI);
        setUI(menuUI.getRoot());
    }

    private void showSettingsUI() {
        var settingsUI = new SettingsUI(this::showMainMenuUI);
        setUI(settingsUI.getRoot());
    }

    private void showGameUI() {
        var gameUI = new MenuUI();
        setUI(gameUI.getRoot());
    }

    // Если вы хотите запускать игру из отдельного main-метода (для тестирования)
    public static void main(String[] args) {
        launch(args);
    }
}