package com.warage.UI.Authentication;

import com.warage.Views.TimeChecker;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.image.Image;

public class LoginUI {
    private AnchorPane root;

    public LoginUI(Runnable onLoginSuccess, Runnable toRegistration) {
        root = new AnchorPane();
        root.getStyleClass().add("root");
        root.getStylesheets().add(getClass().getResource("/Styles/Authentication/Auth.css").toExternalForm());

        // Background image
        ImageView backgroundImage = new ImageView();
        TimeChecker.setBackgroundImage(backgroundImage);
        backgroundImage.setFitWidth(1600);
        backgroundImage.setFitHeight(800);
        backgroundImage.setLayoutX(-14);
        backgroundImage.setLayoutY(-11);
        backgroundImage.setPickOnBounds(true);

        // ПРИВЯЗКА ФОТО К КРАЯМ
        AnchorPane.setTopAnchor(backgroundImage, 0.0);
        AnchorPane.setBottomAnchor(backgroundImage, 0.0);
        AnchorPane.setLeftAnchor(backgroundImage, 0.0);
        AnchorPane.setRightAnchor(backgroundImage, 0.0);
        root.getChildren().add(backgroundImage);

        // Логотип
        ImageView logo = new ImageView(new Image(getClass().getResource("/Image/WarAge.png").toExternalForm()));
        logo.setFitWidth(462);
        logo.setFitHeight(365);
        logo.setLayoutX(590);
        logo.setLayoutY(14);
        logo.setPreserveRatio(true);
        logo.setPickOnBounds(true);
        root.getChildren().add(logo);

        // Метка "Вход"
        Label loginTitle = new Label("Вход");
        loginTitle.setFont(new Font(31));
        loginTitle.setLayoutX(734);
        loginTitle.setLayoutY(360);
        loginTitle.getStyleClass().add("mainLabel");
        root.getChildren().add(loginTitle);

        // Метка "Логин"
        Label loginLabel = new Label("Логин");
        loginLabel.setLayoutX(597);
        loginLabel.setLayoutY(449);
        root.getChildren().add(loginLabel);

        // Поле логина
        TextField loginField = new TextField();
        loginField.setLayoutX(696);
        loginField.setLayoutY(443);
        loginField.getStyleClass().add("text-field");
        root.getChildren().add(loginField);

        // Метка "Пароль"
        Label passwordLabel = new Label("Пароль");
        passwordLabel.setLayoutX(597);
        passwordLabel.setLayoutY(535);
        root.getChildren().add(passwordLabel);

        // Поле пароля
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(700);
        passwordField.setLayoutY(528);
        passwordField.getStyleClass().add("password-field");
        root.getChildren().add(passwordField);

        // Чекбокс "Запомнить меня"
        CheckBox rememberMe = new CheckBox("Запомнить меня на этом устройстве");
        rememberMe.setLayoutX(603);
        rememberMe.setLayoutY(607);
        root.getChildren().add(rememberMe);

        // Кнопка "Войти"
        Button loginButton = new Button("Войти");
        loginButton.setLayoutX(720);
        loginButton.setLayoutY(653);
        loginButton.setPrefSize(116, 48);
        loginButton.setOnAction(e -> {

            onLoginSuccess.run();
        });
        root.getChildren().add(loginButton);

        // Надпись "Ещё не зарегистрированы?"
        Label registerLabel = new Label("Ещё не зарегестрированы?");
        registerLabel.setLayoutX(560);
        registerLabel.setLayoutY(726);
        root.getChildren().add(registerLabel);

        // Кнопка "Зарегистрироваться"
        Button registerButton = new Button("Зарегистрироваться");
        registerButton.setLayoutX(773);
        registerButton.setLayoutY(722);
        registerButton.getStyleClass().add("hyperlink-button");
        registerButton.setOnAction(e -> toRegistration.run());
        root.getChildren().add(registerButton);
    }


    public AnchorPane getRoot(){
        return root;
    }
}
