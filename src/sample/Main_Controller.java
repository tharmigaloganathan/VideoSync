package sample;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.ClientFiles.Client;
import sample.ServerFiles.Server;


import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Main_Controller {

    @FXML
    public ScrollPane scrollPane;
    public VBox vBox;
    public Label lbl;
    public TextField inputField;
    public MediaView mediaView;
    public StackPane mediaPane;
    public Button playButton;
    public Button pauseButton;
    public ImageView playIcon;
    public Label startMedia;
    public Label endMedia;
    public Slider seekSlider;

    MediaPlayer mediaPlayer;
    Media media;
    List<Message> messages = Main.getMessages();
    String username;
    Client client;
    Server server;

    //can tell by username for now
    Boolean isClient;
    Boolean isServer;
    //variable to store this person's username

    //at some point need to create Server or client

    public void initialize() {
        determineServerClient();
        printMessagesInArray();
        printMessagesInArray();
        printMessagesInArray();
        launchMediaView();
    }

    public void determineServerClient () {
        System.out.println(Main.getDisplayName());
        if(Main.getDisplayName().equals("server")) {
            System.out.println("Spawning server...");
            this.isClient = false;
            this.isServer = true;
            server = new Server(this, 6666, "server", "bleh");
        } else {
            System.out.println("Spawning client...");
            this.isClient = true;
            this.isServer = false;
            client = new Client(this, "192.168.0.20", 6666, "client");
        }
    }

    public void printMessagesInArray() {
        Iterator<Message> i = messages.iterator();
        VBox messageVbox;
//        System.out.println("The ArrayList elements are:");
        while (i.hasNext()) {
            Message current = i.next();
//            System.out.println(current.getText() + " - " + current.getUsername());
            messageVbox = createMessage(current.getText(), current.getUsername());
            vBox.getChildren().add(messageVbox);
        }
        scrollPane.setFitToWidth(true);
        vBox.setFillWidth(true);
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(1d);
    }


    public VBox createMessage(String text, String username) {

        VBox messageVbox = new VBox();
        messageVbox.getStyleClass().clear();

        Label messageText = new Label();
        messageText.setText(text);

        messageText.setWrapText(true);

        Label messageUsername = new Label();
        messageUsername.getStyleClass().clear();

        if (username.equals("You")) {
            messageVbox.getStyleClass().add("home-message-vbox");
            messageText.getStyleClass().add("home-message-text");
            messageUsername.getStyleClass().add("home-message-username");
            messageUsername.setText("You");
        } else {
            messageVbox.getStyleClass().add("other-message-vbox");
            messageText.getStyleClass().add("other-message-text");
            messageUsername.getStyleClass().add("other-message-username");
            messageUsername.setText(username);
        }
        messageVbox.getChildren().addAll(messageText, messageUsername);
        return messageVbox;

    }

    public void addNewMessage(String text, String username) {
        VBox messageVbox;
        if (!username.equals(this.username)) {
            messageVbox = createMessage(text, username);
        } else if (username.equals("you")) {
            messageVbox = createMessage(text, "You");
        } else {
            return;
        }
        vBox.getChildren().add(messageVbox);
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(1d);
    }

    @FXML
    public void sendButton () {
        String text = inputField.getText();
        System.out.println(text);
        inputField.clear();
        Message message = new Message(text, "You");
        messages.add(message);
        addNewMessage(text, "You");
        if (isClient) {
            client.sendMessage(message);
        } else {
            server.sendMessage(message);
        }
    }

    @FXML
    public void launchFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Media File");
        Stage mainStage = Main.getPrimaryStage();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            System.out.println(selectedFile.getName());
        }
    }

    public void launchMediaView() {
        mediaView.fitWidthProperty().bind(mediaPane.widthProperty());
        mediaView.fitHeightProperty().bind(mediaPane.heightProperty());
        mediaView.setPreserveRatio(true);

        String path = new File("src/sample/video.mp4").getAbsolutePath();
        System.out.println(new File(path).toURI().toString());
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);

        MediaBar mediaBar = new MediaBar(seekSlider, mediaPlayer, media, startMedia, endMedia, playButton, pauseButton);
        mediaBar.initializeMediaBar();
    }

    @FXML
    public void playButtonPressed () {
        mediaPlayer.play();
    }

    @FXML
    public void pauseButtonPressed () {
        mediaPlayer.pause();
    }
}

class MediaBar {
    Slider seekSlider;
    MediaPlayer mediaPlayer;
    Media media;
    Label startMedia;
    Label endMedia;
    Button playButton;
    Button pauseButton;

    public MediaBar (Slider seekSlider, MediaPlayer mediaPlayer, Media media, Label startMedia, Label endMedia, Button playButton, Button pauseButton) {
        this.seekSlider = seekSlider;
        this.mediaPlayer = mediaPlayer;
        this.media = media;
        this.startMedia = startMedia;
        this.endMedia = endMedia;
        this.playButton = playButton;
        this.pauseButton = pauseButton;

    }

    public void initializeMediaBar () {
        mediaPlayer.setOnReady(() -> {
            initializeSlider();
            initializeTimeLabels();
        });
    }

    public void initializeSlider () {
        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov)
            { updateSliderValuesRunnable(); }
        });

        // Inorder to jump to the certain part of video
        seekSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov)
            {
                if (seekSlider.isPressed())
                    mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(seekSlider.getValue() / 100));
            }
        });
    }

    public void initializeTimeLabels () {
        startMedia.setText("00:00:00");
        endMedia.setText(durationToString(media.getDuration()));
        startMedia.textProperty().bind(
                Bindings.createStringBinding(() ->
                                durationToString(mediaPlayer.getCurrentTime()),
                        mediaPlayer.currentTimeProperty())
        );
    }

    public void initializeButtons () {

    }

    protected void updateSliderValuesRunnable()
    {
        Platform.runLater(new Runnable() {
            public void run()
            {
                // Updating to the new time value
                // This will move the slider while running your video
                seekSlider.setValue(mediaPlayer.getCurrentTime().toMillis() /
                        mediaPlayer.getTotalDuration()
                                .toMillis()
                        * 100);
            }
        });
    }

    public String durationToString(Duration duration) {
        return String.format("%02d:%02d:%02d",
                (int) duration.toHours(),
                (int) duration.toMinutes() % 60,
                (int) duration.toSeconds() % 60);
    }
}
