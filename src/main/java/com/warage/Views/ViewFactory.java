package com.warage.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ViewFactory {
    //Authentication windows
    public void showLoginWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Authentication/Login.fxml"));
        createStage(fxmlLoader);
    }

    public void showRegistrationWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Authentication/Registration.fxml"));
        createStage(fxmlLoader);
    }

    public void showUserAgreementWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Authentication/UserAgreement.fxml"));
        createStage(fxmlLoader);
    }


    //Menu windows
    public void showMainMenuWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/MainMenu.fxml"));
        createStage(fxmlLoader);
    }

    public void showSettingsWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Settings.fxml"));
        createStage(fxmlLoader);
    }

    public void showMenuWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Menu.fxml"));
        createStage(fxmlLoader);
    }

    private void createStage(FXMLLoader fxmlLoader) {
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle("WarAge");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Image/logo.png")));
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
