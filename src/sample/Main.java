package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static Stage primaryStage;
    private static List<Message> messages = new ArrayList<>();

    private static String displayName;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("VideoSync - Login");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void populateMessages() {
        messages.add(new Message("hello, how are you? hello, how are you? hello, how are you?", "You"));
        messages.add(new Message("hello, how are you? hello, how are you? hello, how are you?", "user1"));
        messages.add(new Message("hello", "You"));
        messages.add(new Message("hello", "user1"));
        messages.add(new Message("how are you?", "You"));
        messages.add(new Message("good", "user1"));
        messages.add(new Message("how are you?", "user1"));
        messages.add(new Message("good aswell", "You"));
        messages.add(new Message("anything new?", "user1"));
        messages.add(new Message("nope", "You"));
    }

    public static List<Message> getMessages() {
        return messages;
    }

    public static void main(String[] args) {
        populateMessages(); launch(args);
    }

    public static String getDisplayName () {
        return displayName;
    }

    public static void setDisplayName (String display) {
        displayName = display;
    }
}
