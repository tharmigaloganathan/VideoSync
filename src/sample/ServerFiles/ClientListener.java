package sample.ServerFiles;

import sample.MediaRequest;
import sample.Message;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientListener extends Thread {

    /*
    The Client will send message along with their user name
    This class will have to keep listening to that one client and send any incoming data to Server class

     */

    Server server;
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;

    public ClientListener(Server server, Socket socket, ObjectInputStream input, ObjectOutputStream output) {
        this.server = server;
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    public void run () { //how to use breakpoints
        try {
            Object incomingObject = input.readObject(); //blocking function
            System.out.println("Message from client detected.");
            server.broadcast(incomingObject);
        } catch (Exception e) {
            System.out.println(e + "line 36 Client Listener");
        }
    }

    public ObjectOutputStream getObjectOutputStream () {
        return output;
    }
}
