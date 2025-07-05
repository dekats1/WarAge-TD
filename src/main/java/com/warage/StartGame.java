package com.warage;

import com.warage.Model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartGame extends Application {

    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showMainMenuWindow();
    }
}
