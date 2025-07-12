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
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AchievementWindowUI {
    @Getter
    private AnchorPane root;
    private final Button allAchievementsButton;
    private final Button unCompletedAchievementsButton;
    private final Button completedAchievementsButton;

    private List<PlayerAchievement> allAchievements; // Все данные достижений, загруженные один раз
    private final VBox contentVBox; // Определяем как final, так как он создается один раз

    // Новый список для хранения UI-нод достижений, созданных один раз
    private List<HBox> achievementUIRows;

    public AchievementWindowUI() {
        // Основной корневой контейнер
        root = new AnchorPane();
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Achievements.css")).toExternalForm());

        // Контейнер для скролла с якорями
        AnchorPane scrollContainer = new AnchorPane();
        AnchorPane.setTopAnchor(scrollContainer, 100.0);
        AnchorPane.setBottomAnchor(scrollContainer, 50.0);
        AnchorPane.setLeftAnchor(scrollContainer, 200.0);
        AnchorPane.setRightAnchor(scrollContainer, 200.0);
        scrollContainer.getStyleClass().add("achievement-window-background");

        // Контент внутри скролла - Инициализируем здесь
        contentVBox = new VBox(20.0); // <-- Инициализировано один раз
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
            refreshButtonStyles(allAchievementsButton);
            filterAchievementsUI(null); // Передаем null для отображения всех
        });

        unCompletedAchievementsButton = new Button("Невыполненные достижения");
        unCompletedAchievementsButton.setLayoutX(700.0);
        unCompletedAchievementsButton.setLayoutY(50.0);
        unCompletedAchievementsButton.getStyleClass().add("filter-button");
        root.getChildren().add(unCompletedAchievementsButton);

        //Обработчик нажатия на все достиги
        unCompletedAchievementsButton.setOnAction(event -> {
            refreshButtonStyles(unCompletedAchievementsButton);
            filterAchievementsUI(false); // false для невыполненных
        });

        completedAchievementsButton = new Button("Выполненные достижения");
        completedAchievementsButton.setLayoutX(950.0);
        completedAchievementsButton.setLayoutY(50.0);
        completedAchievementsButton.getStyleClass().add("filter-button");
        root.getChildren().add(completedAchievementsButton);

        //Обработчик нажатия на все достиги
        completedAchievementsButton.setOnAction(event -> {
            refreshButtonStyles(completedAchievementsButton);
            filterAchievementsUI(true); // true для выполненных
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

        // Загрузка данных и инициализация UI-элементов
        PlayerAchievementsApi playerAchievementsApi = new PlayerAchievementsApi();
        playerAchievementsApi.getAllPlayerAchievements().thenAccept(playerAchievements -> {
            this.allAchievements = playerAchievements;
            Platform.runLater(() -> {
                // Создаем все UI-элементы ОДИН раз
                this.achievementUIRows = createAllAchievementUIRows(playerAchievements);
                // Отображаем все достижения по умолчанию
                filterAchievementsUI(null);
            });
        }).exceptionally(e -> {
            System.err.println("Failed to load achievements: " + e.getMessage());
            Platform.runLater(() -> {
                contentVBox.getChildren().clear();
                contentVBox.getChildren().add(new javafx.scene.control.Label("Не удалось загрузить достижения. Попробуйте перезапустить приложение."));
            });
            return null;
        });
    }

    private List<HBox> createAllAchievementUIRows(List<PlayerAchievement> achievements) {
        List<HBox> rows = new ArrayList<>();
        for (int i = 0; i < achievements.size(); i += 2) {
            HBox row = new HBox(30);
            row.setAlignment(Pos.TOP_CENTER);
            PlayerAchievement first = achievements.get(i);
            row.getChildren().add(createAchievementUI(first));
            if (i + 1 < achievements.size()) {
                PlayerAchievement second = achievements.get(i + 1);
                row.getChildren().add(createAchievementUI(second));
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * Фильтрует и отображает UI-элементы достижений.
     *
     * @param isCompleted true для выполненных, false для невыполненных, null для всех.
     */
    private void filterAchievementsUI(Boolean isCompleted) {
        if (achievementUIRows == null) {
            return; // Данные еще не загружены
        }

        List<Node> filteredRows = new ArrayList<>();
        // Здесь мы проходимся по ИСХОДНЫМ данным, но отображаем уже созданные UI-элементы
        for (int i = 0; i < allAchievements.size(); i += 2) {
            PlayerAchievement first = allAchievements.get(i);
            PlayerAchievement second = (i + 1 < allAchievements.size()) ? allAchievements.get(i + 1) : null;

            boolean shouldShowFirst = (isCompleted == null) || (isCompleted && Objects.equals(first.getProgress(), first.getAchievement().getNeedToReward())) || (!isCompleted && first.getProgress() < first.getAchievement().getNeedToReward());

            boolean shouldShowSecond = (second != null) && ((isCompleted == null) || (isCompleted && Objects.equals(second.getProgress(), second.getAchievement().getNeedToReward())) || (!isCompleted && second.getProgress() < second.getAchievement().getNeedToReward()));

            // Берем уже созданную HBox-строку
            HBox currentRow = achievementUIRows.get(i / 2);

            // Теперь нужно управлять видимостью отдельных элементов внутри HBox
            Node firstAchievementNode = currentRow.getChildren().getFirst();
            firstAchievementNode.setVisible(shouldShowFirst);
            firstAchievementNode.setManaged(shouldShowFirst); // setManaged управляет, занимает ли элемент место в макете

            if (second != null) {
                Node secondAchievementNode = currentRow.getChildren().get(1);
                secondAchievementNode.setVisible(shouldShowSecond);
                secondAchievementNode.setManaged(shouldShowSecond);
            }

            // Добавляем строку HBox в отфильтрованный список, если хотя бы один элемент в ней видим
            if (shouldShowFirst || shouldShowSecond) {
                filteredRows.add(currentRow);
            }
        }
        contentVBox.getChildren().setAll(filteredRows); // Обновляем VBox один раз
    }


    // Создание элемента UI (без изменений)
    private Node createAchievementUI(PlayerAchievement element) {
        return Objects.equals(element.getProgress(), element.getAchievement().getNeedToReward()) ? new CompleteAchievementUI(element).getRoot() : new UnCompleteAchievement(element).getRoot();
    }

    private void refreshButtonStyles(Button activeBut) {
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