package com.example.desktopcontroller;

import android.os.AsyncTask;

public class SendMessageToServer extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        //extract the msg and keycode
        String message = params[0];
        String code = params[1];

        int intMessage;
        if (code.equals("STRING")) {
            try {
                //send string msg
                MainActivity.objectOutputStream.writeObject(message);
                MainActivity.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (code.equals("INT")) {
            try {
                //send integer msg
                intMessage = Integer.parseInt(message);
                MainActivity.objectOutputStream.writeObject(intMessage);
                MainActivity.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}