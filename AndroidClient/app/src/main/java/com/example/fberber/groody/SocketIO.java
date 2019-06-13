package com.example.fberber.groody;

import java.io.*;
import java.net.Socket;

public final class SocketIO {

    private Socket socket;
    private OutputStream output;
    private InputStream input;

    public SocketIO(){
        socket = null;
        output = null;
        input = null;
    }

    public Socket getSoc(){return socket;}

    public void connect(final String ip, final int port) throws IOException {
        close();
        socket = new Socket(ip, port);
        output = new BufferedOutputStream(socket.getOutputStream());
        input = new BufferedInputStream(socket.getInputStream());
    }

    public boolean isConnected() {
        if (socket == null){
            return false;
        }
        else if (socket.isConnected()){
            return true;
        }
        else {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            socket = null;
            return false;
        }
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (output != null) {
            output.close();
        }
        if (input != null) {
            input.close();
        }
    }

    public void write(byte[] bytes) throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        output.write(bytes);
    }

    public void flush() throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        output.flush();
    }

    public void read(byte[] bytes) throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        input.read(bytes);
    }

    public int read(byte[] bytes, int i, int remaining) throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        return input.read(bytes, i, remaining);
    }
}