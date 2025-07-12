package com.warage.UI.Achievements;

import com.warage.Model.PlayerAchievement;
import com.warage.Service.PlayerAchievementsApi;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AchievementWindowUI {
    private AnchorPane root;
    private  Button allAchievementsButton;
    private Button unCompletedAchievementsButton;
    private Button completedAchievementsButton;
    private PlayerAchievementsApi playerAchievementsApi = new PlayerAchievementsApi();

    private List<PlayerAchievement> allAchievements;

    public AchievementWindowUI() {
        // Основной корневой контейнер
        root = new AnchorPane();
        root.getStylesheets().add(getClass().getResource("/Styles/Achievements.css").toExternalForm());

        // Контейнер для скролла с якорями
        AnchorPane scrollContainer = new AnchorPane();
        AnchorPane.setTopAnchor(scrollContainer, 100.0);
        AnchorPane.setBottomAnchor(scrollContainer, 50.0);
        AnchorPane.setLeftAnchor(scrollContainer, 200.0);
        AnchorPane.setRightAnchor(scrollContainer, 200.0);
        scrollContainer.getStyleClass().add("achievement-window-background");

        // Контент внутри скролла
        VBox contentVBox = new VBox(20.0);
        contentVBox.setAlignment(Pos.TOP_CENTER);
        contentVBox.setPadding(new Insets(20.0));
        contentVBox.setPrefWidth(830.0);

        // ScrollPane, обертывающий VBox
        ScrollPane scrollPane = new ScrollPane(contentVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(650);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        scrollContainer.getChildren().add(scrollPane);

        // Добавление контейнера в root
        root.getChildren().add(scrollContainer);

        // Кнопки фильтра
        allAchievementsButton = new Button("Все достижения");
        allAchievementsButton.setLayoutX(550.0);
        allAchievementsButton.setLayoutY(50.0);
        allAchievementsButton.getStyleClass().add("selected-button");
        root.getChildren().add(allAchievementsButton);

        //Обработчик нажатия на все достиги
        allAchievementsButton.setOnAction(event -> {
            contentVBox.getChildren().clear();
            refreshButtonStyles(allAchievementsButton);
            refreshAllAchievements(contentVBox);
        });

        unCompletedAchievementsButton = new Button("Невыполненные достижения");
        unCompletedAchievementsButton.setLayoutX(700.0);
        unCompletedAchievementsButton.setLayoutY(50.0);
        unCompletedAchievementsButton.getStyleClass().add("filter-button");
        root.getChildren().add(unCompletedAchievementsButton);

        //Обработчик нажатия на все достиги
        unCompletedAchievementsButton.setOnAction(event -> {
            contentVBox.getChildren().clear();
            refreshButtonStyles(unCompletedAchievementsButton);
            refreshUnCompletedAchievements(contentVBox);
        });

        completedAchievementsButton = new Button("Выполненные достижения");
        completedAchievementsButton.setLayoutX(950.0);
        completedAchievementsButton.setLayoutY(50.0);
        completedAchievementsButton.getStyleClass().add("filter-button");
        root.getChildren().add(completedAchievementsButton);

        //Обработчик нажатия на все достиги
        completedAchievementsButton.setOnAction(event -> {
            contentVBox.getChildren().clear();
            refreshButtonStyles(completedAchievementsButton);
            refreshCompletedAchievements(contentVBox);
        });

        // Кнопка закрытия
        Button closeButton = new Button("✕");
        closeButton.setLayoutX(1365.0);
        closeButton.setLayoutY(75.0);
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> {
            ((StackPane) root.getParent()).getChildren().remove(root);
        });
        root.getChildren().add(closeButton);

        // ТУТ ВЫЗОВ РЕНДЕРИНГА
        playerAchievementsApi.getAllPlayerAchievements().thenAccept(playerAchievements -> {
            this.allAchievements = playerAchievements;
            Platform.runLater(() -> {
                refreshAllAchievements(contentVBox);
                contentVBox.applyCss();
                contentVBox.layout();
                refreshAllAchievements(contentVBox);
            });
        });

    }

    public AnchorPane getRoot() {return root;}

    // Заполнение всех достижений
    private void refreshAllAchievements(VBox contentVBox) {
        for (int i = 0; i < allAchievements.size(); i += 2) {
            HBox row = new HBox(30);
            row.setAlignment(Pos.TOP_CENTER);
            PlayerAchievement first = allAchievements.get(i);
            row.getChildren().add(createAchievementUI(first));
            if (i + 1 < allAchievements.size()) {
                PlayerAchievement second = allAchievements.get(i + 1);
                row.getChildren().add(createAchievementUI(second));
            }
            contentVBox.getChildren().add(row);
        }
    }

    // Метод для заполнения выполненных достижек
    private void refreshCompletedAchievements(VBox contentVBox) {
        List<PlayerAchievement> completedAchievements = allAchievements.stream().filter(achievement->achievement.getDateAchieved()!=null).collect(Collectors.toList());
        for(int i = 0; i < completedAchievements.size(); i += 2) {
            HBox row = new HBox(30);
            row.setAlignment(Pos.TOP_CENTER);
            PlayerAchievement first = completedAchievements.get(i);
            row.getChildren().add(createAchievementUI(first));
            if (i + 1 < completedAchievements.size()) {
                PlayerAchievement second = completedAchievements.get(i + 1);
                row.getChildren().add(createAchievementUI(second));
            }
            contentVBox.getChildren().add(row);
        }
    }

    // Метод для заполнения невыполненных достижек
    private void refreshUnCompletedAchievements(VBox contentVBox) {
        List<PlayerAchievement> unCompletedAchievements = allAchievements.stream().filter(achievement -> achievement.getDateAchieved()==null).collect(Collectors.toList());
        for(int i = 0; i<unCompletedAchievements.size(); i += 2) {
            HBox row = new HBox(30);
            row.setAlignment(Pos.TOP_CENTER);
            PlayerAchievement first = unCompletedAchievements.get(i);
            row.getChildren().add(createAchievementUI(first));
            if(i+1<unCompletedAchievements.size()) {
                PlayerAchievement second = unCompletedAchievements.get(i + 1);
                row.getChildren().add(createAchievementUI(second));
            }
            contentVBox.getChildren().add(row);
        }
    }

    // Создание элемента UI
    private Node createAchievementUI(PlayerAchievement element) {
        return element.getDateAchieved()!=null
                ? new CompleteAchievementUI(element).getRoot()
                : new UnCompleteAchievement(element).getRoot();
    }


    private void refreshButtonStyles(Button activeBut){
        allAchievementsButton.getStyleClass().remove("selected-button");
        allAchievementsButton.getStyleClass().add("filter-button");

        completedAchievementsButton.getStyleClass().remove("selected-button");
        completedAchievementsButton.getStyleClass().add("filter-button");

        unCompletedAchievementsButton.getStyleClass().remove("selected-button");
        unCompletedAchievementsButton.getStyleClass().add("filter-button");

        activeBut.getStyleClass().remove("filter-button");
        activeBut.getStyleClass().add("selected-button");
    }
}
