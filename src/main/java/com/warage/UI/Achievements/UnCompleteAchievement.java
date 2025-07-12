package com.warage.UI.Achievements;

import com.warage.Model.PlayerAchievement;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class UnCompleteAchievement {
    private AnchorPane root;
    private Rectangle progressBarFill;
    private Label progressText;

    public UnCompleteAchievement(PlayerAchievement element) {
        root = new AnchorPane();
        root.setPrefSize(429, 141);
        root.getStyleClass().add("uncompleted-achievement-tile");
        root.getStylesheets().add(getClass().getResource("/Styles/Achievements.css").toExternalForm());
        root.setPadding(new Insets(10));

        // Фон плитки
        Rectangle background = new Rectangle(429, 141);
        background.setArcWidth(30);
        background.setArcHeight(30);
        background.setFill(Color.web("#4A4A4A"));
        background.setStroke(Color.TRANSPARENT);

        // Аватар серый
        Circle avatarCircle = new Circle(45);
        avatarCircle.setFill(Color.web("#808080"));

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

        // Заголовок
        Label titleLabel = new Label(element.getAchievement().getName());
        titleLabel.setLayoutX(125);
        titleLabel.setLayoutY(12);
        titleLabel.setPrefSize(296, 56);
        titleLabel.setWrapText(true);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("System Bold", 24));
        titleLabel.getStyleClass().add("achievement-title");

        // Описание
        Label descriptionLabel = new Label(element.getAchievement().getDescription());
        descriptionLabel.setLayoutX(125);
        descriptionLabel.setLayoutY(59);
        descriptionLabel.setPrefSize(289, 45);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(Color.web("#CCCCCC"));
        descriptionLabel.setFont(Font.font(14));
        descriptionLabel.getStyleClass().add("achievement-description");

        // Прогресс-бар
        StackPane progressBar = new StackPane();
        progressBar.setLayoutX(125);
        progressBar.setLayoutY(100);
        progressBar.setPrefSize(200, 20);

        Rectangle progressBarBackground = new Rectangle(200, 15);
        progressBarBackground.setArcWidth(20);
        progressBarBackground.setArcHeight(20);
        progressBarBackground.setFill(Color.web("#606060"));
        progressBarBackground.setStroke(Color.TRANSPARENT);

        progressBarFill = new Rectangle(110, 15);
        calculateProgressLine(progressBarBackground,element.getProgress(),element.getAchievement().getNeedToReward());
        progressBarFill.setArcWidth(20);
        progressBarFill.setArcHeight(20);
        progressBarFill.setFill(Color.web("#00BFFF"));
        progressBarFill.setStroke(Color.TRANSPARENT);
        StackPane fillWrapper = new StackPane(progressBarFill);
        fillWrapper.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        progressText = new Label(element.getProgress()+"/"+element.getAchievement().getNeedToReward());
        progressText.setTextFill(Color.WHITE);
        progressText.setFont(Font.font(12));
        progressText.getStyleClass().add("achievement-progress");

        progressBar.getChildren().addAll(progressBarBackground, fillWrapper, progressText);
        root.getChildren().addAll(background, avatarBox, titleLabel, descriptionLabel, progressBar);
    }


    private void calculateProgressLine(Rectangle progressBarBackground,int now,int need) {
        double width =  (double) now/need;
        progressBarFill.setWidth(progressBarBackground.getWidth()*width);
    }

    public AnchorPane getRoot() {
        return root;
    }

}
