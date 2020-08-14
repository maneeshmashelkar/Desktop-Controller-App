package com.example.desktopcontroller;

import android.os.AsyncTask;

public class SendMessageToServer extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        String message = params[0];
        String code = params[1];

        // message may be int, float, long or string
        int intMessage;
//        System.out.println(message + ", " + code);
        if (code.equals("STRING")) {
            try {
                MainActivity.objectOutputStream.writeObject(message);
                MainActivity.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
//                MainActivity.socketException();
            }
        } else if (code.equals("INT")) {
            try {
                intMessage = Integer.parseInt(message);
                MainActivity.objectOutputStream.writeObject(intMessage);
                MainActivity.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
//                MainActivity.socketException();
            }

        }
        return null;
    }
}