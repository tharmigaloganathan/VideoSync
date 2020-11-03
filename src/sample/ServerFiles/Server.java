package sample.ServerFiles;

import sample.Main_Controller;
import sample.MediaRequest;
import sample.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    /*
    The Server has port forwarding setup, openly allows remote clients to connect to it
    Messages from clients get sent to the Server, which sends to everyone else
    Playback commands from clients also get to the Server, which forwards to everyone else
     */

    /*
    Need to create a separate class that will continuously listen for new connections - ConnectionsListener (1)
    Need to create a separate class that will continuously listen to current connections for any messages - ClientListener (n)
    This thread will be used to send messages from the server?
     */

    Main_Controller mainController;
    String ip;
    int portNumber;
    String username;
    String groupname;

    ConnectionsListener connectionsListener; // thread that listens for client connection requests
    List<ClientListener> clients = new ArrayList<ClientListener>(); // stores references to all clients
    //list to store references of all clients, messages will be forwarded to all

    public Server (Main_Controller mainController, int portNumber, String username, String groupname) {
        this.mainController = mainController;
        this.portNumber = portNumber;
        this.username = username;
        this.groupname = groupname;
        startServer();
    }

    public void startServer () {
        try {
            System.out.println("Server is started.");
            ServerSocket serverSocket = new ServerSocket(portNumber);
            connectionsListener = new ConnectionsListener(serverSocket, this);
            connectionsListener.start(); //starts connections Listener thread
            System.out.println("Now listening for connections.");
        } catch (Exception e) {
            System.out.println(e + "line 54 Server");
        }
    }

    public void sendMessage (Message message) {
        System.out.println("Broadcasting message from server.");
        broadcast(message);
    }

    /*
    ** Function called from ConnectionsListener thread
     */
    public void newConnection (Socket socket, ObjectInputStream input, ObjectOutputStream output) {
        System.out.println("Created client listener for this client.");
        ClientListener clientListener = new ClientListener(this, socket, input, output);
        clients.add(clientListener); //add the client listener to the array
        clientListener.start(); //begin listening to requests from client (start the thread)
    }

    /*
    ** Function called from a ClientListener thread
     */
    public void broadcast (Object object) {
        System.out.println("Broadcasting message to each client. hi");
        if (object instanceof Message) { //displays message on server's screen
            System.out.println("i am a message");
            Message message = (Message) object;
            mainController.addNewMessage(message.getText(), message.getUsername());
        } else {
            System.out.println("object is not instanceof Message");
        }
        System.out.println("object is not instanceof Message");
        for (ClientListener cl : clients) {
            ObjectOutputStream output = cl.getObjectOutputStream();
            try {
                output.writeObject(object);
            } catch (Exception e) {
                System.out.println(e + "line 87 Server");
            }
        }
    }
}
