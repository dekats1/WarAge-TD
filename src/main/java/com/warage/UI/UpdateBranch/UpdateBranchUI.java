package com.warage.UI.UpdateBranch;

import com.warage.Model.Tower;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import lombok.Data;

@Data
public class UpdateBranchUI {
    private AnchorPane root;
    private Tower pickedTower;

    public UpdateBranchUI() {
        root = new AnchorPane();
        root.setPrefHeight(607.0);
        root.setPrefWidth(390.0);
        root.getStyleClass().add("update-branch-root");
        root.getStylesheets().add(getClass().getResource("/Styles/UpdateBranch.css").toExternalForm());
    }

    public void setTower(Tower tower) {
        this.pickedTower = tower;
        updateUI();
    }

    private void updateUI() {
        root.getChildren().clear();
        ImageView towerImage = new ImageView(new Image(getClass().getResource(pickedTower.getAssetPath()).toExternalForm()));
        towerImage.getStyleClass().add("tower-image");
        towerImage.setFitWidth(100);
        towerImage.setFitHeight(100);
        towerImage.setLayoutX(145);
        towerImage.setLayoutY(50);

        createArrows(towerImage);

        root.getChildren().add(towerImage);
    }

    private void createArrows(ImageView towerImage) {
        double startX = towerImage.getLayoutX() + towerImage.getFitWidth() / 2;
        double startY = towerImage.getLayoutY() + towerImage.getFitHeight();
        double arrowLength = 100;

        double[] arrowPositions = {startX - 40, startX, startX + 40};

        for (double arrowX : arrowPositions) {
            // Линия стрелки
            Line line = new Line();
            line.getStyleClass().add("upgrade-arrow");
            line.setStartX(arrowX);
            line.setStartY(startY);
            line.setEndX(arrowX);
            line.setEndY(startY + arrowLength);


            Polygon arrowHead = new Polygon();
            arrowHead.getStyleClass().add("arrow-head");
            arrowHead.getPoints().addAll(
                    arrowX, startY + arrowLength,
                    arrowX - 7, startY + arrowLength - 15,
                    arrowX + 7, startY + arrowLength - 15
            );

            root.getChildren().addAll(line, arrowHead);
        }
    }
}