package sample.ServerFiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionsListener extends Thread {

    ServerSocket serverSocket;
    Server server;

    /*
    Socket is the connection itself
    ServerSocket is what listens?
     */

    public ConnectionsListener (ServerSocket serverSocket, Server server) {
        this.serverSocket = serverSocket;
        this.server = server;
    }

    public void run() {
        while(true) {
            try { //create input and out put statement for it
                Socket socket = serverSocket.accept(); // this is a blocking statement?
                System.out.println("Connection initiated with client.");
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Output Stream Created");
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                System.out.println("Input Stream Created");
                server.newConnection(socket, input, output);
            } catch (Exception e) {
                System.out.println(e + "line 36 Connection Listener");
            }

        }

    }
}
