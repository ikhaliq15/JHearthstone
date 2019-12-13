package me.imrankhaliq.jhearthstone.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class UserConnection {

    private DataInputStream mInputStream;
    private DataOutputStream mOutputStream;

    public UserConnection(DataInputStream inputStream, DataOutputStream outputStream) {
        this.mInputStream = inputStream;
        this.mOutputStream = outputStream;

        System.out.println("A new user connection has been formed!");
    }
}
