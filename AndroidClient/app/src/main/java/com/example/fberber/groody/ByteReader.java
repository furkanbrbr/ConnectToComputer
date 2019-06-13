package com.example.fberber.groody;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ByteReader implements Runnable {

    private DataInputStream myInputStream;
    private final SocketIO io;

    public ByteReader(final SocketIO serverIo) {
        io = serverIo;

    }

    @Override
    public final void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (!io.isConnected()) {
                    Thread.sleep(100);
                    continue;
                }
                System.err.println("-----------ByteReader , Before Read----------");

                InputStreamReader streamReader= new InputStreamReader(io.getSoc().getInputStream());
                BufferedReader reader= new BufferedReader(streamReader);
                String value= reader.readLine();
                System.err.println("------------ByteReader , After Read------");
                System.out.println(value);
                int valueInt = Integer.parseInt(value);
                Mediator.med=valueInt;
                reader.close();
                Thread.sleep(100);
                break ;
            }
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
