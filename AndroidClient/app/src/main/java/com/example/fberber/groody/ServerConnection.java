package com.example.fberber.groody;

import java.io.IOException;
import java.util.Queue;

public final class ServerConnection implements Runnable {

    private final String ip;

    private final int port;

    private final SocketIO io;

    private final Thread writer;


    public ServerConnection(final String ipAddress, final int portNumber,final Queue<byte[]> outgoing,int mode) {
        ip = ipAddress;
        port = portNumber;
        io = new SocketIO();
        if(mode==1)
            writer = new Thread(new ByteWriter(io, outgoing));
        else{
            writer = new Thread(new ByteReader(io));
        }
    }

    @Override
    public void run() {
        writer.start();
        try {
            io.connect(ip, port);
            while (!Thread.currentThread().isInterrupted()) {
                if (!io.isConnected()) {
                    break;
                }
                Thread.sleep(100);
            }
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            io.close();
            writer.interrupt();
            writer.join();

        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}