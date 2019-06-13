package com.example.fberber.groody;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;

public final class ByteWriter implements Runnable {

    private final Queue<byte[]> queue;
    private final SocketIO io;

    public ByteWriter(final SocketIO serverIo, final Queue<byte[]> source) {
        io = serverIo;
        queue = source;
    }

    @Override
    public final void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (io.isConnected()){
                    if(!queue.isEmpty()) {
                        do {
                            final byte[] sendBytes = queue.remove();
                            io.write(ByteBuffer.allocate(4).putInt(sendBytes.length).array());
                            io.write(sendBytes);
                            Thread.sleep(50);
                        } while (queue.size() > 0);
                        io.flush();
                    }
                }
                Thread.sleep(100);
            }
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}