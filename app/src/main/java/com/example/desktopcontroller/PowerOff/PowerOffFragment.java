package com.example.desktopcontroller.PowerOff;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.desktopcontroller.MainActivity;
import com.example.desktopcontroller.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class PowerOffFragment extends Fragment implements View.OnClickListener {

    Button shutdownButton, restartButton, sleepButton, lockButton;
    DialogInterface.OnClickListener dialogClickListener;
    AlertDialog.Builder builder;
    String action;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poweroff,container,false);

        shutdownButton = (Button) view.findViewById(R.id.shutdownButton);
        shutdownButton.setOnClickListener(this);

        restartButton = (Button) view.findViewById(R.id.restartButton);
        restartButton.setOnClickListener(this);

        sleepButton = (Button) view.findViewById(R.id.sleepButton);
        sleepButton.setOnClickListener(this);

        lockButton = (Button) view.findViewById(R.id.lockButton);
        lockButton.setOnClickListener(this);

        //confirmation dialog box
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //send msg to server
                        MainActivity.sendMessageToServer(action);
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }

            }
        };
        builder = new AlertDialog.Builder(getActivity());
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        //get the msg of the button
        switch(id) {
            case R.id.shutdownButton:
                action = "SHUTDOWN";
                break;
            case R.id.restartButton:
                action = "RESTART";
                break;
            case R.id.sleepButton:
                action = "SLEEP";
                break;
            case R.id.lockButton:
                action = "LOCK";
                break;
        }
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        builder.setTitle(action)
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}
