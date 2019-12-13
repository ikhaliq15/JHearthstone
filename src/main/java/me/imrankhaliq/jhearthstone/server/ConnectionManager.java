package me.imrankhaliq.jhearthstone.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManager {

    ArrayList<UserConnection> userConnections;

    public ConnectionManager(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            userConnections = new ArrayList<>();
            while (true) {
                Socket incomingConnection = server.accept();
                InputStream incomingDataStream = incomingConnection.getInputStream();
                OutputStream outgoingDataStream = incomingConnection.getOutputStream();
                DataInputStream inputStream = new DataInputStream(new BufferedInputStream(incomingDataStream));
                DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(outgoingDataStream));
                new UserConnection(inputStream, outputStream);
            }
        } catch (IOException i) {
            System.out.println(i);
        }
    }
}
