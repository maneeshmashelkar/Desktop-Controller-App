package com.example.desktopcontroller.Connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.desktopcontroller.MainActivity;
import com.example.desktopcontroller.MakeConnection;
import com.example.desktopcontroller.R;


import java.net.Socket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConnectFragment extends Fragment {
    private EditText IP,PORT;
    private Button connect;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_connect, container, false);

        IP = (EditText) rootview.findViewById(R.id.ipEditview);
        PORT = (EditText) rootview.findViewById(R.id.portEditview);
        connect = (Button) rootview.findViewById(R.id.connectbtn);

        sharedPreferences = getActivity().getSharedPreferences("lastConnectionDetails", Context.MODE_PRIVATE);
        String lastConnectionDetails[] = getConnectionDetails();
        IP.setText(lastConnectionDetails[0]);
        PORT.setText(lastConnectionDetails[1]);

        if (MainActivity.clientSocket != null) {
            connect.setText("connected");
            connect.setEnabled(false);
        }

        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeConnection();
            }
        });
        return rootview;
    }




    public void makeConnection() {

        String ipAddress = IP.getText().toString();
        String port =PORT.getText().toString();
        setConnectionDetails(new String[] {ipAddress, port});
        connect.setText("Connecting...");
        connect.setEnabled(false);
            new MakeConnection(ipAddress, port, getActivity()) {
                @Override
                public void receiveData(Object result) {
                    MainActivity.clientSocket = (Socket) result;
                    if (MainActivity.clientSocket == null) {
                        Toast.makeText(getActivity(), "Server is not listening", Toast.LENGTH_SHORT).show();
                        connect.setText("connect");
                        connect.setEnabled(true);
                    } else {
                        connect.setText("connected");
                    }
                }

            }.execute();

    }

    private String[] getConnectionDetails() {
        String arr[] = new String[2];
        arr[0] = sharedPreferences.getString("lastConnectedIP", "");
        arr[1] = sharedPreferences.getString("lastConnectedPort", "3000");
        return arr;
    }
    private void setConnectionDetails(String arr[]) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastConnectedIP", arr[0]);
        editor.putString("lastConnectedPort", arr[1]);
        editor.apply();
    }


}
