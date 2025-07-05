package com.warage.Controllers;

import com.warage.Views.TimeChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MenuController {

    @FXML
    private Button awardsButton;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button companyButton;

    @FXML
    private Button giftButton;

    @FXML
    private Button heroesButton;

    @FXML
    private Button shopButton;

    @FXML
    private Button srartGameButton;


    public void initialize() {
        TimeChecker.setBackgroundImageOnMain(backgroundImage);

    }
}
