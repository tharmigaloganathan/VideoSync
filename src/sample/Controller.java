package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.Iterator;
import java.util.List;

public class Controller {

//    private Main_Controller mainController;

    @FXML
    public Label lbl;
    public TextField displayNameField;

    @FXML
    public void getStarted(ActionEvent event) {
        Main.setDisplayName(displayNameField.getText());
        System.out.println(lbl.getText());
        Stage primaryStage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource( "main.fxml" ));
            primaryStage.setScene(new Scene(root,900, 600));
            primaryStage.setTitle( "VideoSync - Main" );
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e + "line 24");
        }
//        mainController.displayMessages();
    }


}
