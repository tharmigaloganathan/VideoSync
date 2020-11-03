package sample.ClientFiles;

import sample.Message;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener extends Thread {

    Client client;
    Socket connection;
    ObjectInputStream input;

    public ServerListener (Client client, Socket connection, ObjectInputStream input) {
        this.client = client;
        this.connection = connection;
        this.input = input;
    }

    public void run () {
        while (true) {
            try {
                Object incomingObject = input.readObject();
                System.out.println("Received message from server.");
                if (incomingObject instanceof Message) {
                    client.receiveMessage(incomingObject);
                } else {
                    client.receiveMediaRequest();
                }
            } catch (Exception e) {

            }
        }

    }

}
