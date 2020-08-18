package com.example.desktopcontroller.Presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.desktopcontroller.MainActivity;
import com.example.desktopcontroller.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PresentationFragment extends Fragment implements View.OnClickListener {

    Button downButton, upButton, presentationButton, leftButton, rightButton;
    boolean flag=true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presentation,container,false);

        downButton = (Button) view.findViewById(R.id.downButton);
        downButton.setOnClickListener(this);

        upButton = (Button) view.findViewById(R.id.upButton);
        upButton.setOnClickListener(this);

        leftButton = (Button) view.findViewById(R.id.leftButton);
        leftButton.setOnClickListener(this);

        rightButton = (Button) view.findViewById(R.id.rightButton);
        rightButton.setOnClickListener(this);

        presentationButton = (Button) view.findViewById(R.id.presentationButton);
        presentationButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String action = "PRESENTATION_KEY";

        //get msg of the button clicked
        switch (id) {
            case R.id.upButton:
                action = "UP_KEY";
                break;
            case R.id.downButton:
                action = "DOWN_KEY";
                break;
            case R.id.presentationButton:
                Log.i("TAG", "onClick: "+presentationButton.getBackground());
                if(flag){
                    action = "PRESENTATION_MAX_KEY";
                    flag=false;
                    //set "fullscreen exit" background image
                    presentationButton.setBackgroundResource(R.drawable.fullscreen_exit_black_24dp);
                }else{
                    action = "PRESENTATION_MIN_KEY";
                    flag=true;
                    //set "fullscreen" background image
                    presentationButton.setBackgroundResource(R.drawable.fullscreen_black_24dp);
                }
                break;
            case R.id.leftButton:
                action = "LEFT_KEY";
                break;
            case R.id.rightButton:
                action = "RIGHT_KEY";
                break;

        }
        //send the msg to server
        MainActivity.sendMessageToServer(action);
    }
}
