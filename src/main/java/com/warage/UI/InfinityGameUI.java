package com.warage.UI;

import com.almasb.fxgl.app.GameController;
import com.warage.Model.Tower;
import com.warage.UI.InterfaceUI.PauseMenuUI;
import com.warage.UI.ShopInGame.HeroPurchaseListener;
import com.warage.UI.ShopInGame.ShopForGameUI;
import com.warage.UI.UpdateBranch.UpdateBranchUI;
import com.warage.Utils.SlideAnimations;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InfinityGameUI implements HeroPurchaseListener {
    private AnchorPane root;
    private ImageView background;
    private ImageView road;
    private ImageView closeButShop;
    private Pane unitsLayer;
    private ShopForGameUI shopForGameUI;
    private boolean isPaused = false;
    private boolean isBranchesOpened = false;
    private PauseMenuUI pauseMenuUI;
    private UpdateBranchUI updateBranch = new UpdateBranchUI();

    private Tower pendingHeroToPlace = null;
    private ImageView ghostHeroImage = null;

    private GameController gameController;
    private Map<ImageView,Tower> placedTowers = new HashMap<>();


    public InfinityGameUI(Runnable toMenuUI, GameController gameController) {
        this.gameController = gameController;

        root = new AnchorPane();
        root.setPrefWidth(1600.0);
        root.setPrefHeight(800.0);


        background = new ImageView(new Image(getClass().getResource("/Image/Maps/InfinityMap/InfinityMap.png").toExternalForm()));
        background.setFitWidth(1600);
        background.setFitHeight(800);
        background.setPickOnBounds(true);
        AnchorPane.setTopAnchor(background, 0.0);
        AnchorPane.setBottomAnchor(background, 0.0);
        AnchorPane.setLeftAnchor(background, 0.0);
        AnchorPane.setRightAnchor(background, 0.0);

        road = new ImageView(new Image(getClass().getResource("/Image/Maps/InfinityMap/InfinityRoad.png").toExternalForm()));
        road.setFitHeight(800);
        road.setFitWidth(1600);

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
        closeButShop = new ImageView(new Image(getClass().getResource("/Image/InterfaceIcons/exitRedImage.png").toExternalForm()));
        closeButShop.setFitHeight(50);
        closeButShop.setFitWidth(50);
        closeButShop.setPickOnBounds(true);
        closeButShop.setLayoutX(1160);
        closeButShop.setPreserveRatio(true);

        closeButShop.setOnMouseClicked(event-> {
            closeButShop.setVisible(false);
            SlideAnimations.slideOutToRight(shopForGameUI.getRoot());
        });

        ImageView openShopImage = new ImageView(new Image(getClass().getResource("/Image/InterfaceIcons/ShopIcon.png").toExternalForm()));
        openShopImage.setFitHeight(50);
        openShopImage.setFitWidth(50);
        openShopImage.setPickOnBounds(true);
        openShopImage.setLayoutX(1470);
        openShopImage.setLayoutY(5);
        openShopImage.setPreserveRatio(true);

        openShopImage.setOnMouseClicked(event-> {
           closeButShop.setVisible(true);
           SlideAnimations.slideOutToLeft(shopForGameUI.getRoot());
        });

        shopForGameUI = new ShopForGameUI();
        shopForGameUI.setHeroPurchaseListener(this);
        AnchorPane.setRightAnchor(shopForGameUI.getRoot(), 0.0);
        AnchorPane.setTopAnchor(shopForGameUI.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(shopForGameUI.getRoot(), 0.0);

        // Update Branch
        updateBranch.getRoot().setVisible(false);
        updateBranch.getRoot().setPrefWidth(390);
        AnchorPane.setRightAnchor(updateBranch.getRoot(), -390.0);
        AnchorPane.setTopAnchor(updateBranch.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(updateBranch.getRoot(), 0.0);

        root.getChildren().addAll(background, unitsLayer,road,pauseImage, openShopImage, shopForGameUI.getRoot(),closeButShop, updateBranch.getRoot());

        unitsLayer.setOnMouseClicked(this::handleMapClick);
        unitsLayer.setOnMouseMoved(this::handleMouseMovement);
    }

    @Override
    public void onHeroPurchase(Tower hero) {
        if (this.pendingHeroToPlace == null) {
            this.pendingHeroToPlace = hero;
            showGhostHero(hero);
            System.out.println("Герой выбран! Кликните по карте, чтобы разместить его.");
        } else {
            this.pendingHeroToPlace = hero;
            showGhostHero(hero);
            System.out.println("Выбран новый герой! Кликните по карте, чтобы разместить его.");
        }
    }


    private void showGhostHero(Tower hero) {
        if (ghostHeroImage != null) {
            unitsLayer.getChildren().remove(ghostHeroImage);
        }

        ghostHeroImage = new ImageView();
        try {
            ghostHeroImage.setImage(new Image(getClass().getResource(hero.getAssetPath()).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке изображения призрака героя " + hero.getAssetPath() + ": " + e.getMessage());
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
            ghostHeroImage.setLayoutX(event.getX() - ghostHeroImage.getFitWidth()/2);
            ghostHeroImage.setLayoutY(event.getY() - ghostHeroImage.getFitHeight()/2);
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
        } else if(isBranchesOpened){
            closeButShop.setVisible(true);
            isBranchesOpened = false;
            updateBranch.getRoot().setVisible(false);
            SlideAnimations.slideOutToRight(updateBranch.getRoot());
        }
    }

    private void placeHeroOnMap(Tower hero, double x, double y) {
        ImageView actualHeroImage = new ImageView();
        try {
            actualHeroImage.setImage(new Image(getClass().getResource(hero.getAssetPath()).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке изображения героя " + hero.getAssetPath() + ": " + e.getMessage());
            return;
        }
        placedTowers.put(actualHeroImage, hero);
        actualHeroImage.setFitWidth(50);
        actualHeroImage.setFitHeight(50);
        actualHeroImage.setLayoutX(x - actualHeroImage.getFitWidth());
        actualHeroImage.setLayoutY(y - actualHeroImage.getFitHeight());
        shopForGameUI.updateGold(hero.getBaseCost());
        unitsLayer.getChildren().add(actualHeroImage);

        actualHeroImage.setOnMouseClicked(event->{
            event.consume();
            handleMouseClickOnTower(event);
        });

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
        root.getChildren().add(pauseMenuUI.getRoot());
    }


    private void handleMouseClickOnTower(MouseEvent event) {
        if(event.getTarget() instanceof ImageView) {
            ImageView selectedHeroImage = (ImageView) event.getTarget();
            Tower selectedTower = placedTowers.get(selectedHeroImage);
            if(selectedTower != null) {
                closeButShop.setVisible(false);
                isBranchesOpened = true;
                updateBranch.setTower(selectedTower);
                SlideAnimations.slideInFromRight(updateBranch.getRoot(),root.getWidth());
            }
        }
    }

}