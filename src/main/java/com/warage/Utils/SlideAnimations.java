package com.warage.Utils;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class SlideAnimations {

    public static void slideOutToRight(Node node) {
        double width = node.getLayoutBounds().getWidth();

        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), node);
        slide.setFromX(0);
        slide.setToX(width);

        slide.setOnFinished(event->{
            node.setVisible(false);
            node.setTranslateX(0);
        });
        slide.play();
    }

    public static void slideOutToLeft(Node node) {
        double width = node.getLayoutBounds().getWidth();

        node.setTranslateX(width);
        node.setVisible(true);

        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), node);
        slide.setFromX(width);
        slide.setToX(0);
        slide.play();
    }

    public static void slideInFromRight(Node node, double parentWidth) {
        // Сбрасываем трансформации
        node.setTranslateX(0);
        node.setVisible(true);

        // Устанавливаем начальное положение за экраном
        node.setLayoutX(parentWidth);

        // Анимация
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.3), node);
        slide.setFromX(0);
        slide.setToX(-node.getBoundsInLocal().getWidth());
        slide.play();
    }
}
