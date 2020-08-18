package com.example.desktopcontroller;

import android.content.Context;
import android.os.AsyncTask;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public abstract class MakeConnection extends AsyncTask<Void, Void, Socket>implements CallbackReceiver
{
    private String ipAddress, port;
    private Socket clientSocket;

    protected MakeConnection(String ipAddress, String port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    protected Socket doInBackground(Void... params) {
        try {
            int portNumber = Integer.parseInt(port);
            SocketAddress socketAddress = new InetSocketAddress(ipAddress, portNumber);
            clientSocket = new Socket();
            // wait for 3 second
            clientSocket.connect(socketAddress, 3000);
            MainActivity.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch(Exception e) {
            e.printStackTrace();
            clientSocket = null;
        }
        return clientSocket;
    }

    protected void onPostExecute(Socket clientSocket) {
        receiveData(clientSocket);
    }


    @Override
    public abstract void receiveData(Object result);


}
