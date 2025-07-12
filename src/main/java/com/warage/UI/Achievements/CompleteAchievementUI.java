package com.warage.UI.Achievements;

import com.warage.Model.PlayerAchievement;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class CompleteAchievementUI {
    private AnchorPane root;
    public CompleteAchievementUI(PlayerAchievement element) {
        root = new AnchorPane();
        root.setPrefSize(429, 141);
        root.getStyleClass().add("achievement-tile");
        root.getStylesheets().add(getClass().getResource("/Styles/Achievements.css").toExternalForm());
        root.setPadding(new Insets(7, 7, 7, 7));

        // Прямоугольный фон
        Rectangle background = new Rectangle(430, 140);
        background.setArcWidth(30);
        background.setArcHeight(30);
        background.setFill(Color.web("#20B2AA"));
        background.setStroke(Color.TRANSPARENT);

        // Аватар в круге
        Circle avatarCircle = new Circle(45);
        avatarCircle.setFill(Color.web("#FFD700"));

        ImageView avatarImage = new ImageView();
        avatarImage.setFitWidth(130);
        avatarImage.setFitHeight(130);
        avatarImage.setPreserveRatio(true);
        avatarImage.setPickOnBounds(true);
        avatarImage.setImage(new Image(getClass().getResourceAsStream(element.getAchievement().getPhotoPath())));

        StackPane avatarStack = new StackPane(avatarCircle, avatarImage);
        avatarStack.setPrefSize(100, 100);

        HBox avatarBox = new HBox(20, avatarStack);
        avatarBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Заголовок достижения
        Label titleLabel = new Label(element.getAchievement().getName());
        titleLabel.setLayoutX(131);
        titleLabel.setWrapText(true);
        titleLabel.setLayoutY(8);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("System Bold", 24));
        titleLabel.getStyleClass().add("achievement-title");

        // Описание достижения
        Label descriptionLabel = new Label(element.getAchievement().getDescription());
        descriptionLabel.setLayoutX(138);
        descriptionLabel.setLayoutY(48);
        descriptionLabel.setPrefSize(289, 45);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(Color.web("#E0FFFF"));
        descriptionLabel.setFont(Font.font(14));
        descriptionLabel.getStyleClass().add("achievement-description");

        // Прогресс или дата получения
        Label progressLabel = new Label("Получено: "+ element.getDateAchieved());
        progressLabel.setLayoutX(138);
        progressLabel.setLayoutY(102);
        progressLabel.setTextFill(Color.web("#BBF0F0"));
        progressLabel.setFont(Font.font(12));
        progressLabel.getStyleClass().add("achievement-progress");
        root.getChildren().addAll(background, avatarBox, titleLabel, descriptionLabel, progressLabel);
    }

    public AnchorPane getRoot() {
        return root;
    }
}
