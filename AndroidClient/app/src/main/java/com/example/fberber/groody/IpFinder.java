package com.example.fberber.groody;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class IpFinder implements Runnable {

    private Socket mSocket;
    private String mHost;
    private int	mPort;
    private Boolean mConnected;

    public IpFinder(String host, int port) {
        mHost = host;
        mPort = port;
        mSocket = null;
        mConnected = false;
    }

    public boolean connect(String host, int port){
        mHost = host;
        mPort = port;
        mSocket = null;
        Thread thread = new Thread(this);
        thread.start();
        return true;
    }

    public boolean disconnect(){

        try {
            mSocket.close();
        } catch (IOException e) {
            System.err.println("------IpFinder.disconnect() errorr-----");
        }
        return  false ;
    }


    @Override
    public void run() {
        try{
            System.out.println("-----------------------manuel00---"+mHost+"----"+mPort);
            mSocket = new Socket(mHost, mPort);
            System.out.println("-----------------------manuel01---"+mHost+"----"+mPort);
            mConnected = true;
            Mediator.ipcontrol=mHost ;
            Mediator.ipflag=1;
            Thread.currentThread().interrupt();
        } catch (UnknownHostException uhe) {
            //System.err.println("----UnknownHostException----");
            Thread.currentThread().interrupt();
        } catch (IOException ioe) {
            //System.err.println("----IOException----");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            //System.err.println("---Exception-----");
            Thread.currentThread().interrupt();
        }
    }
}
