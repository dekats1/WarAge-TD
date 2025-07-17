package com.warage.UI.ShopInGame;

import com.almasb.fxgl.dsl.FXGL;
import com.warage.Model.Tower;
import com.warage.Service.TowerApi;
import com.warage.Utils.SlideAnimations;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import lombok.Data;
import javafx.geometry.Insets;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
public class ShopForGameUI {
    private AnchorPane root;
    private ScrollPane heroesScrollPane;
    private FlowPane heroesFlowPane;
    private Label goldAmountLabel;
    private HeroPurchaseListener purchaseListener;
    private int coins = 10000000;
    private TowerApi towerApi = new TowerApi();
    private List<Tower> allTowers = new ArrayList<>();

    public void setHeroPurchaseListener(HeroPurchaseListener listener) {
        this.purchaseListener = listener;
    }


    public ShopForGameUI() {
        towerApi.getAllTowers().thenAccept(heroes->{
            allTowers.addAll(heroes);
        });
        root = new AnchorPane();
        root.setPrefHeight(607.0);
        root.setPrefWidth(390.0);

        URL cssUrl = getClass().getResource("/Styles/ShopInGame.css");
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Ошибка: CSS-файл ShopInGame.css не найден. Проверьте путь: /Styles/ShopInGame.css");
        }
        root.getStyleClass().add("shop-root-pane");

        // --- Кнопки категорий ---
        Button swordsmenButton = new Button("Мечники");
        swordsmenButton.setLayoutX(43.0);
        swordsmenButton.setLayoutY(2.0);
        swordsmenButton.setMnemonicParsing(false);
        swordsmenButton.getStyleClass().add("category-button");

        Button archersButton = new Button("Стрелки");
        archersButton.setLayoutX(149.0);
        archersButton.setLayoutY(2.0);
        archersButton.setMnemonicParsing(false);
        archersButton.getStyleClass().add("category-button");

        Button magesButton = new Button("Маги");
        magesButton.setLayoutX(255.0);
        magesButton.setLayoutY(2.0);
        magesButton.setMnemonicParsing(false);
        magesButton.getStyleClass().add("category-button");

        // --- Разделительные линии ---
        Line line1 = new Line();
        line1.setStartX(-100.0);
        line1.setEndX(243.0);
        line1.setLayoutX(101.0);
        line1.setLayoutY(50.0);
        line1.getStyleClass().add("shop-separator-line");

        Line line2 = new Line();
        line2.setStartX(-100.0);
        line2.setEndX(243.0);
        line2.setLayoutX(101.0);
        line2.setLayoutY(97.0);
        line2.getStyleClass().add("shop-separator-line");

        // --- Отображение золота ---
        ImageView coinImage = new ImageView();
        coinImage.setImage(new Image(getClass().getResourceAsStream("/Image/Coin.png")));
        coinImage.setFitHeight(30);
        coinImage.setFitWidth(30);
        coinImage.setPreserveRatio(true);
        coinImage.setLayoutX(135.0);
        coinImage.setLayoutY(57.0);

        this.goldAmountLabel = new Label(String.valueOf(this.coins));
        this.goldAmountLabel.setLayoutX(190.0);
        this.goldAmountLabel.setLayoutY(60.0);
        this.goldAmountLabel.getStyleClass().add("gold-amount-label");

        final double HORIZONTAL_GAP = 10.0;
        final double FLOWPANE_PADDING = 10.0;

        heroesFlowPane = new FlowPane();
        heroesFlowPane.setHgap(HORIZONTAL_GAP);
        heroesFlowPane.setVgap(10.0);
        heroesFlowPane.setPadding(new Insets(FLOWPANE_PADDING));
        heroesFlowPane.setPrefWidth(360);
        heroesFlowPane.setPrefWrapLength(310);
        heroesFlowPane.getStyleClass().add("heroes-flow-pane");

        // --- Инициализация ScrollPane ---
        heroesScrollPane = new ScrollPane();
        heroesScrollPane.setContent(heroesFlowPane);
        heroesScrollPane.setFitToWidth(true);
        AnchorPane.setLeftAnchor(heroesScrollPane, 0.0);
        AnchorPane.setRightAnchor(heroesScrollPane, 0.0);
        AnchorPane.setBottomAnchor(heroesScrollPane, 0.0);
        AnchorPane.setTopAnchor(heroesScrollPane, 80.0);
        heroesScrollPane.getStyleClass().add("heroes-scroll-pane");
        root.getChildren().addAll(
                swordsmenButton,
                archersButton,
                magesButton,
                line1,
                line2,
                coinImage,
                this.goldAmountLabel,
                heroesScrollPane
        );

        refreshHeroes();
    }

    // Создание UI отображения героя
    private Pane createHeroUI(Tower tower) {
        Pane heroPane = new Pane();
        heroPane.setPrefSize(150, 180);
        heroPane.getStyleClass().add("hero-card-pane");

        // Изображение героя
        ImageView heroImageView = new ImageView();
        heroImageView.setFitHeight(100.0);
        heroImageView.setFitWidth(100.0);
        heroImageView.setPreserveRatio(true);
        heroImageView.setPickOnBounds(true);
        heroImageView.setLayoutX(25.0);
        heroImageView.setLayoutY(10.0);

        try {
            URL heroImageUrl = getClass().getResource(tower.getAssetPath());
            if (heroImageUrl != null) {
                heroImageView.setImage(new Image(heroImageUrl.toExternalForm()));
            } else {
                System.err.println("Ошибка: Изображение героя не найдено по пути: " + tower.getAssetPath());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке изображения героя " + tower.getAssetPath() + ": " + e.getMessage());
        }

        // Стоимость героя
        Label costLabel = new Label(String.valueOf(tower.getBaseCost()));
        costLabel.setLayoutX(50.0);
        costLabel.setLayoutY(145.0);
        costLabel.getStyleClass().add("hero-cost-label");
        heroPane.setOnMouseClicked(event->{
            if (coins >= tower.getBaseCost()) {
                if(purchaseListener != null) {
                    purchaseListener.onHeroPurchase(tower);
                }
            } else {
                FXGL.getNotificationService().pushNotification("НЕДОСТАТОЧНО ДЕНЕГ");
            }
        });

        ImageView coinImage = new ImageView();
        coinImage.setImage(new Image(getClass().getResourceAsStream("/Image/Coin.png")));
        coinImage.setFitHeight(17);
        coinImage.setFitWidth(17);
        coinImage.setPreserveRatio(true);
        coinImage.setLayoutX(80.0);
        coinImage.setLayoutY(146.0);
        heroPane.getChildren().addAll(heroImageView,costLabel,coinImage);
        return heroPane;
    }

    /**
     * Обновляет отображение героев в магазине, располагая их по 2 в строке.
     * FlowPane автоматически управляет расположением.
     */
    private void refreshHeroes() {
        heroesFlowPane.getChildren().clear(); // Очищаем предыдущие карточки героев
        for (Tower tow : allTowers) {
            Pane heroUI = createHeroUI(tow);
            heroesFlowPane.getChildren().add(heroUI);
        }
    }

    public void updateGold(int cost) {
        if (goldAmountLabel != null) {
            coins = coins - cost;
            goldAmountLabel.setText(String.valueOf(coins));
        }
    }
}