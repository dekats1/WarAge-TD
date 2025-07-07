package com.warage.UI.Authentication;

import com.warage.Views.TimeChecker;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

public class RegistrationUI {
    private AnchorPane root;

    public RegistrationUI(Runnable toLoginUI) {
        // Создание основы
        root = new AnchorPane();
        root.setPrefHeight(800.0);
        root.setPrefWidth(1017.0);
        root.getStylesheets().add(getClass().getResource("/Styles/Authentication/Auth.css").toExternalForm());

        // Задний фон
        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(1600.0);
        backgroundImage.setLayoutX(-1.0);
        backgroundImage.setPickOnBounds(true);
        TimeChecker.setBackgroundImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // Кнопка регистрации
        Button registrationButton = new Button("Зарегистрироваться");
        registrationButton.setLayoutX(763.0);
        registrationButton.setLayoutY(657.0);
        registrationButton.setMnemonicParsing(false);
        root.getChildren().add(registrationButton);

        // Кнопка перехода на "Логин"
        Button loginButton = new Button("Войти");
        loginButton.setLayoutX(923.0);
        loginButton.setLayoutY(724.0);
        loginButton.setMnemonicParsing(false);
        loginButton.getStyleClass().add("hyperlink-button");
        root.getChildren().add(loginButton);

        // Поле для огина
        TextField loginField = new TextField();
        loginField.setLayoutX(777.0);
        loginField.setLayoutY(346.0);
        root.getChildren().add(loginField);

        // Поле пароля
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(778.0);
        passwordField.setLayoutY(480.0);
        root.getChildren().add(passwordField);

        // Пол подтверждения пароля
        PasswordField verifyPasswordField = new PasswordField();
        verifyPasswordField.setLayoutX(780.0);
        verifyPasswordField.setLayoutY(550.0);
        root.getChildren().add(verifyPasswordField);

        // Поле для почты
        TextField emailField = new TextField();
        emailField.setLayoutX(779.0);
        emailField.setLayoutY(413.0);
        root.getChildren().add(emailField);

        // РЕГИСТРАЦИЯ текст
        Label mainLabel = new Label("Регистрация");
        mainLabel.setLayoutX(752.0);
        mainLabel.setLayoutY(274.0);
        mainLabel.getStyleClass().add("mainLabel");
        root.getChildren().add(mainLabel);

        // Label "Уже зарегистрированы?"
        Label registeredLabel = new Label("Уже зарегистрированы?");
        registeredLabel.setLayoutX(732.0);
        registeredLabel.setLayoutY(729.0);
        root.getChildren().add(registeredLabel);

        // Label "Повторите пароль"
        Label repeatPasswordLabel = new Label("Повторите пароль");
        repeatPasswordLabel.setLayoutX(622.0);
        repeatPasswordLabel.setLayoutY(558.0);
        root.getChildren().add(repeatPasswordLabel);

        // Label "Пароль"
        Label passwordLabel = new Label("Пароль");
        passwordLabel.setLayoutX(701.0);
        passwordLabel.setLayoutY(484.0);
        root.getChildren().add(passwordLabel);

        // Label "Логин"
        Label loginLabel = new Label("Логин");
        loginLabel.setLayoutX(705.0);
        loginLabel.setLayoutY(353.0);
        root.getChildren().add(loginLabel);

        // Label "Почта"
        Label emailLabel = new Label("Почта");
        emailLabel.setLayoutX(703.0);
        emailLabel.setLayoutY(419.0);
        root.getChildren().add(emailLabel);

        // CheckBox "Ознакомлен и согласен с"
        CheckBox rulesCheckBox = new CheckBox("Ознакомлен и согласен с");
        rulesCheckBox.setLayoutX(630.0);
        rulesCheckBox.setLayoutY(617.0);
        rulesCheckBox.setMnemonicParsing(false);
        root.getChildren().add(rulesCheckBox);

        // Button "пользовательским соглашением"
        Button rulesButton = new Button("пользовательским соглашением");
        rulesButton.setLayoutX(835.0);
        rulesButton.setLayoutY(615.0);
        rulesButton.setMnemonicParsing(false);
        rulesButton.getStyleClass().add("hyperlink-button");
        root.getChildren().add(rulesButton);

        // Иконка
        ImageView warAgeImage = new ImageView();
        warAgeImage.setFitHeight(328.0);
        warAgeImage.setFitWidth(309.0);
        warAgeImage.setLayoutX(699.0);
        warAgeImage.setLayoutY(-11.0);
        warAgeImage.setPickOnBounds(true);
        warAgeImage.setPreserveRatio(true);
        warAgeImage.setImage(new Image(getClass().getResourceAsStream("/Image/WarAge.png")));
        root.getChildren().add(warAgeImage);

        loginButton.setOnAction(e -> toLoginUI.run());
    }

    public Pane getRoot(){
        return root;
    }
}
