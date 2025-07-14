package com.warage.UI;

import com.almasb.fxgl.app.GameController;
import com.almasb.fxgl.entity.GameWorld;
import com.warage.Model.Tower;
import com.warage.TowerDefenseGameApp;
import com.warage.UI.InterfaceUI.PauseMenuUI;
import com.warage.UI.ShopInGame.HeroPurchaseListener;
import com.warage.UI.ShopInGame.ShopForGameUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane; 
import javafx.scene.input.MouseEvent;
import lombok.Data;

@Data
public class InfinityGameUI implements HeroPurchaseListener {
    private AnchorPane root;
    private ImageView background;
    private Pane unitsLayer;
    private ShopForGameUI shopForGameUI;
    private boolean isPaused = false;
    private PauseMenuUI pauseMenuUI;

    private ShopForGameUI.Heroes pendingHeroToPlace = null;
    private ImageView ghostHeroImage = null;

    private GameController gameController;

    public InfinityGameUI(Runnable toMenuUI, GameController gameController) {
        this.gameController = gameController;

        root = new AnchorPane();
        root.setPrefWidth(1600.0);
        root.setPrefHeight(800.0);


        background = new ImageView(new Image(getClass().getResource("/Image/InfinityMap.png").toExternalForm()));
        background.setFitWidth(1600);
        background.setFitHeight(800);
        background.setPickOnBounds(true);
        AnchorPane.setTopAnchor(background, 0.0);
        AnchorPane.setBottomAnchor(background, 0.0);
        AnchorPane.setLeftAnchor(background, 0.0);
        AnchorPane.setRightAnchor(background, 0.0);

        // Pause
        ImageView pauseImage = new ImageView(new Image(getClass().getResource("/Image/InterfaceIcons/pause.png").toExternalForm()));
        pauseImage.setFitWidth(50);
        pauseImage.setFitHeight(50);
        pauseImage.setPickOnBounds(true);
        pauseImage.setPreserveRatio(true);
        pauseImage.setLayoutX(10);
        pauseImage.setLayoutY(10);
        pauseImage.setOnMouseClicked(event-> {
            if(isPaused){
                return;
            }
            isPaused = true;
            pauseGame(toMenuUI);

        });

        //PlayGround
        unitsLayer = new Pane();
        unitsLayer.setPrefSize(1600, 800);
        AnchorPane.setTopAnchor(unitsLayer, 0.0);
        AnchorPane.setBottomAnchor(unitsLayer, 0.0);
        AnchorPane.setLeftAnchor(unitsLayer, 0.0);
        AnchorPane.setRightAnchor(unitsLayer, 0.0);

        // Shop
        shopForGameUI = new ShopForGameUI();
        shopForGameUI.setHeroPurchaseListener(this);
        AnchorPane.setRightAnchor(shopForGameUI.getRoot(), 0.0);
        AnchorPane.setTopAnchor(shopForGameUI.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(shopForGameUI.getRoot(), 0.0);

        root.getChildren().addAll(background, unitsLayer,pauseImage, shopForGameUI.getRoot());

        unitsLayer.setOnMouseClicked(this::handleMapClick);
        unitsLayer.setOnMouseMoved(this::handleMouseMovement);
        unitsLayer.setOnMouseExited(event -> {
            if (ghostHeroImage != null) {
                unitsLayer.getChildren().remove(ghostHeroImage);
                ghostHeroImage = null;
            }
        });
    }

    @Override
    public void onHeroPurchase(ShopForGameUI.Heroes hero) {
        if (this.pendingHeroToPlace == null) {
            this.pendingHeroToPlace = hero;
            showGhostHero(hero);
            System.out.println("Геро выбран! Кликните по карте, чтобы разместить его.");
        } else {
            System.out.println("Вы уже держите героя. Сначала разместите его.");
        }
    }


    private void showGhostHero(ShopForGameUI.Heroes hero) {
        if (ghostHeroImage != null) {
            unitsLayer.getChildren().remove(ghostHeroImage);
        }

        ghostHeroImage = new ImageView();
        try {
            ghostHeroImage.setImage(new Image(getClass().getResource(hero.getPhotoPath()).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке изображения призрака героя " + hero.getPhotoPath() + ": " + e.getMessage());
            return;
        }

        ghostHeroImage.setFitWidth(70);
        ghostHeroImage.setFitHeight(70);
        ghostHeroImage.setOpacity(0.6);
        ghostHeroImage.setMouseTransparent(true);
        unitsLayer.getChildren().add(ghostHeroImage);
    }

    private void handleMouseMovement(MouseEvent event) {
        if (pendingHeroToPlace != null && ghostHeroImage != null) {
            ghostHeroImage.setLayoutX(event.getX() - ghostHeroImage.getFitWidth() );
            ghostHeroImage.setLayoutY(event.getY() - ghostHeroImage.getFitHeight());
        }
    }

    private void handleMapClick(MouseEvent event) {
        if (pendingHeroToPlace != null) {
            double clickX = event.getX();
            double clickY = event.getY();

            placeHeroOnMap(pendingHeroToPlace, clickX, clickY);

            // Очищаем состояние после размещения
            pendingHeroToPlace = null;
            if (ghostHeroImage != null) {
                unitsLayer.getChildren().remove(ghostHeroImage);
                ghostHeroImage = null;
            }
        }
    }

    private void placeHeroOnMap(ShopForGameUI.Heroes hero, double x, double y) {
        ImageView actualHeroImage = new ImageView();
        try {
            actualHeroImage.setImage(new Image(getClass().getResource(hero.getPhotoPath()).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке изображения героя " + hero.getPhotoPath() + ": " + e.getMessage());
            return;
        }

        actualHeroImage.setFitWidth(50);
        actualHeroImage.setFitHeight(50);
        actualHeroImage.setLayoutX(x - actualHeroImage.getFitWidth());
        actualHeroImage.setLayoutY(y - actualHeroImage.getFitHeight());
        shopForGameUI.updateGold(hero.getCost());
        unitsLayer.getChildren().add(actualHeroImage); // Добавляем героя в слой юнитов

        // Cоздать и добавить в игру не только визуальное представление,

    }


    private void pauseGame(Runnable toMenuUI) {
        gameController.pauseEngine();
        pauseMenuUI = new PauseMenuUI(
                ()->{
                    gameController.resumeEngine();
                    toMenuUI.run();
                },
                ()->{
                    gameController.resumeEngine();
                    // СДЕЛАТЬ ЛОГИКУ РЕСТАРТА ИГРЫ
                },
                ()->{
                    isPaused = false;
                    pauseMenuUI.getRoot().setVisible(false);
                    pauseMenuUI.getRoot().setDisable(true);
                    gameController.resumeEngine();
                }
        );
        root.getChildren().add(pauseMenuUI.getRoot());}
}