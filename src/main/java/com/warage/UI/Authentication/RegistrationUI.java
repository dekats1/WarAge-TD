package com.warage.UI.Authentication;

import com.warage.Model.PlayerProfile; // Убедитесь, что этот импорт есть
import com.warage.Service.PlayerApiClient; // Убедитесь, что этот импорт есть
import com.warage.Views.TimeChecker;
import com.warage.Model.Model; // Убедитесь, что этот импорт есть

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.regex.Pattern; // Для EMAIL_PATTERN

public class RegistrationUI {
    private AnchorPane root;

    // Сделаем эти поля членами класса, чтобы они были доступны во всех методах экземпляра
    private TextField loginField;
    private PasswordField passwordField;
    private PasswordField verifyPasswordField;
    private TextField emailField;
    private CheckBox rulesCheckBox; // Добавлено, чтобы сделать доступным

    private PlayerApiClient playerApiClient; // Добавим экземпляр клиента API
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public RegistrationUI(Runnable toLoginUI, Runnable toMainWindowUI) {
        this.playerApiClient = new PlayerApiClient();

        root = new AnchorPane();
        root.setPrefHeight(800.0);
        root.setPrefWidth(1017.0);
        root.getStylesheets().add(getClass().getResource("/Styles/Authentication/Auth.css").toExternalForm());

        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(1600.0);
        backgroundImage.setPickOnBounds(true);
        TimeChecker.setBackgroundImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        Button registrationButton = new Button("Зарегистрироваться");
        registrationButton.setLayoutX(683.0);
        registrationButton.setLayoutY(657.0);
        registrationButton.setMnemonicParsing(false);
        registrationButton.getStyleClass().add("auth");
        registrationButton.setOnAction(e -> registation(registrationButton, toMainWindowUI));
        root.getChildren().add(registrationButton);

        Button loginButton = new Button("Войти");
        loginButton.setLayoutX(843.0);
        loginButton.setLayoutY(724.0);
        loginButton.setMnemonicParsing(false);
        loginButton.getStyleClass().add("hyperlink");
        loginButton.setOnAction(e -> toLoginUI.run());
        root.getChildren().add(loginButton);

        loginField = new TextField();
        loginField.setLayoutX(697.0);
        loginField.setLayoutY(346.0);
        root.getChildren().add(loginField);

        passwordField = new PasswordField();
        passwordField.setLayoutX(698.0);
        passwordField.setLayoutY(480.0);
        root.getChildren().add(passwordField);

        verifyPasswordField = new PasswordField();
        verifyPasswordField.setLayoutX(700.0);
        verifyPasswordField.setLayoutY(550.0);
        root.getChildren().add(verifyPasswordField);

        emailField = new TextField();
        emailField.setLayoutX(699.0);
        emailField.setLayoutY(413.0);
        root.getChildren().add(emailField);

        Label mainLabel = new Label("Регистрация");
        mainLabel.setLayoutX(672.0);
        mainLabel.setLayoutY(274.0);
        mainLabel.getStyleClass().add("mainLabel");
        root.getChildren().add(mainLabel);

        Label registeredLabel = new Label("Уже зарегистрированы?");
        registeredLabel.setLayoutX(652.0);
        registeredLabel.setLayoutY(729.0);
        root.getChildren().add(registeredLabel);

        Label repeatPasswordLabel = new Label("Повторите пароль");
        repeatPasswordLabel.setLayoutX(542.0);
        repeatPasswordLabel.setLayoutY(558.0);
        root.getChildren().add(repeatPasswordLabel);

        Label passwordLabel = new Label("Пароль");
        passwordLabel.setLayoutX(621.0);
        passwordLabel.setLayoutY(484.0);
        root.getChildren().add(passwordLabel);

        Label loginLabel = new Label("Логин");
        loginLabel.setLayoutX(625.0);
        loginLabel.setLayoutY(353.0);
        root.getChildren().add(loginLabel);

        Label emailLabel = new Label("Почта");
        emailLabel.setLayoutX(623.0);
        emailLabel.setLayoutY(419.0);
        root.getChildren().add(emailLabel);

        rulesCheckBox = new CheckBox("Ознакомлен и согласен с");
        rulesCheckBox.setLayoutX(550.0);
        rulesCheckBox.setLayoutY(617.0);
        rulesCheckBox.setMnemonicParsing(false);
        root.getChildren().add(rulesCheckBox);

        Button rulesButton = new Button("пользовательским соглашением");
        rulesButton.setLayoutX(755.0);
        rulesButton.setLayoutY(615.0);
        rulesButton.setMnemonicParsing(false);
        rulesButton.getStyleClass().add("hyperlink");
        root.getChildren().add(rulesButton);

        ImageView warAgeImage = new ImageView();
        warAgeImage.setFitHeight(328.0);
        warAgeImage.setFitWidth(309.0);
        warAgeImage.setLayoutX(619.0);
        warAgeImage.setLayoutY(-11.0);
        warAgeImage.setPickOnBounds(true);
        warAgeImage.setPreserveRatio(true);
        warAgeImage.setImage(new Image(getClass().getResourceAsStream("/Image/WarAge.png")));
        root.getChildren().add(warAgeImage);
    }

    private void registation(Button registrationButton, Runnable toMainWindowUI) { // Убрано static
        String username = loginField.getText().trim(); // Теперь доступно
        String password = passwordField.getText(); // Теперь доступно
        String verifyPassword = verifyPasswordField.getText(); // Теперь доступно
        String email = emailField.getText().trim(); // Теперь доступно

        // 1. Клиентская валидация
        if (username.isEmpty() || password.isEmpty() || verifyPassword.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ошибка регистрации", "Пожалуйста, заполните все поля.");
            return;
        }

        if (username.length() < 3 || username.length() > 20) {
            showAlert(Alert.AlertType.WARNING, "Ошибка регистрации", "Имя пользователя должно быть от 3 до 20 символов.");
            return;
        }

        if (!password.equals(verifyPassword)) {
            showAlert(Alert.AlertType.WARNING, "Ошибка регистрации", "Пароли не совпадают.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Ошибка регистрации", "Пароль должен быть не менее 6 символов.");
            return;
        }
        // Можно добавить более сложную валидацию пароля (цифры, спецсимволы и т.д.)

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert(Alert.AlertType.WARNING, "Ошибка регистрации", "Пожалуйста, введите корректный адрес электронной почты.");
            return;
        }

        if (!rulesCheckBox.isSelected()) { // Теперь доступно
            showAlert(Alert.AlertType.WARNING, "Ошибка регистрации", "Вы должны принять правила пользователя.");
            return;
        }

        registrationButton.setDisable(true);

        playerApiClient.registerNewPlayer(username, password, email)
                .thenAccept(playerProfile -> {
                    Platform.runLater(() -> {
                        if (playerProfile != null) {
                            toMainWindowUI.run();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Ошибка регистрации", "Неизвестная ошибка при регистрации. Проверьте консоль.");
                        }
                        registrationButton.setDisable(false); // Включаем кнопку обратно
                    });
                })
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        String errorMessage = "Произошла ошибка при регистрации.";
                        if (e.getCause() != null && e.getCause().getMessage() != null) {
                            if (e.getCause().getMessage().contains("Response body: ")) {
                                errorMessage = e.getCause().getMessage().substring(e.getCause().getMessage().indexOf("Response body: ") + "Response body: ".length()).trim();
                            } else {
                                errorMessage = e.getCause().getMessage();
                            }
                        }
                        showAlert(Alert.AlertType.ERROR, "Ошибка регистрации", errorMessage);
                        registrationButton.setDisable(false); // Включаем кнопку обратно
                    });
                    return null;
                });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Pane getRoot(){
        return root;
    }
}