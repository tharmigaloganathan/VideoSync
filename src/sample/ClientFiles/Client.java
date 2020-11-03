package sample.ClientFiles;

import javafx.application.Platform;
import sample.Main_Controller;
import sample.Message;

import java.io.*;
import java.net.Socket;

public class Client {

    Main_Controller mainController;
    String serverIP;
    int serverPort;
    String username;

    Socket connection;
    ObjectInputStream input;
    ObjectOutputStream output;
    ServerListener serverListener;

    public Client (Main_Controller mainController, String serverIP, int serverPort, String username) {
        this.mainController = mainController;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.username = username;
        startClient();
    }

    public void startClient () {
        try {
            System.out.println("Client is started.");
            connection = new Socket (serverIP, serverPort);
            input = new ObjectInputStream(connection.getInputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
            System.out.println("Now listening for messages from server.");
            serverListener = new ServerListener(this, connection, input);
            serverListener.start();
        } catch (Exception e) {
            System.out.println();
        }
    }

    //gets called from Main_Controller
    public void sendMessage (Message message) {
        //send a message to the server
        //write to output'
        try {
            System.out.println("Writing message from you to output stream.");
            output.writeObject(message);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void receiveMessage (Object object) {
        //this shoudl call another function in Main_Controller
        //in order to output to the screen
        Message message = (Message) object;
        Platform.runLater(new Runnable () {
            @Override
            public void run() {
                mainController.addNewMessage(message.getText(), message.getUsername());
            }
        });

    }

    public void sendMediaRequest () {

    }

    public void receiveMediaRequest () {
        System.out.println("Received Media Request.");
    }
}
