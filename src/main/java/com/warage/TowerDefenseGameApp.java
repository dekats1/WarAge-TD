package com.warage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TowerDefenseGameApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("My Tower Defense Game");
        settings.setVersion("0.1");
        settings.setFullScreenAllowed(false);
        // Дополнительные настройки FXGL
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
        // Здесь будет FXGL UI, например, счетчик денег, волн, панель выбора башен
        // FXGL имеет свою систему UI, но вы можете использовать и стандартные JavaFX контролы
//        Label moneyLabel = new Label("Деньги: " + FXGL.getip("money"));
//        moneyLabel.setFont(Font.font(20));
//        moneyLabel.setTextFill(Color.GOLD);
//        FXGL.addUINode(moneyLabel, 10, 10);
    }

    // Если вы хотите запускать игру из отдельного main-метода (для тестирования)
    public static void main(String[] args) {
        launch(args);
    }
}