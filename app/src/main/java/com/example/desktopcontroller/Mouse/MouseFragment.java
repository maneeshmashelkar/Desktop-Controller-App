package com.example.desktopcontroller.Mouse;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.desktopcontroller.MainActivity;
import com.example.desktopcontroller.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MouseFragment extends Fragment {
    private TextView mousepad;
    private Button leftclick, rightclick;
    private int initX, initY, disX, disY, moveResultX, moveResultY;
    private boolean mouseMoved = false, moultiTouch = false;
    private float mLastMoveX = Float.MAX_VALUE;
    private float mLastMoveY = Float.MAX_VALUE;
    private long mLastMoveTime;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mouse, container, false);

        mousepad = (TextView) rootView.findViewById(R.id.mousePad);
        leftclick = (Button) rootView.findViewById(R.id.leftClick);
        rightclick = (Button) rootView.findViewById(R.id.rightClick);

        leftclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLeftClick();
            }
        });
        rightclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateRightClick();
            }
        });

        mousepad.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainActivity.clientSocket != null) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            //save X and Y positions when user touches the TextView
                            initX = (int) event.getX();
                            initY = (int) event.getY();
                            moveResultX = 0;
                            moveResultY = 0;
                            Log.i("MOUSETOUCH", "onTouch: " + initX + "," + initY);
                            mouseMoved = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (moultiTouch == false) {
                                disX = (int) event.getX() - initX; //Mouse movement in x direction
                                disY = (int) event.getY() - initY; //Mouse movement in y direction
                                Log.i("EVENT", "latest" + event.getX() + "," + event.getY());
                                Log.i("MOUSEMOVE", "onmove: " + disX + "," + disY);
                                        /*set init to new position so that continuous mouse movement
                                        is captured*/
                                initX = (int) event.getX();
                                initY = (int) event.getY();
                                if (disX != 0 || disY != 0) {
                                    MainActivity.sendMessageToServer("MOUSE_MOVE");
                                    //send mouse movement to server
                                    MainActivity.sendMessageToServer(disX);
                                    MainActivity.sendMessageToServer(disY);
                                    mouseMoved = true;
                                }
                            } else {
                                disY = (int) event.getY() - initY; //Mouse movement in y direction
                                disY = disY / 3;//to scroll by less amount
                                initY = (int) event.getY();
                                if (disY != 0) {
                                    MainActivity.sendMessageToServer("MOUSE_WHEEL");
                                    MainActivity.sendMessageToServer(disY);
                                    mouseMoved = true;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            //consider a tap only if user did not move mouse after ACTION_DOWN
                            if (!mouseMoved) {
                                MainActivity.sendMessageToServer("LEFT_CLICK");
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            initY = (int) event.getY();
                            mouseMoved = false;
                            moultiTouch = true;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            if (!mouseMoved) {
                                MainActivity.sendMessageToServer("LEFT_CLICK");
                            }
                            moultiTouch = false;
                            break;
                    }
                }
                return true;
            }


        });
        return rootView;
    }

    private void simulateLeftClick() {
        String message = "LEFT_CLICK";
        MainActivity.sendMessageToServer(message);
    }

    private void simulateRightClick() {
        String message = "RIGHT_CLICK";
        MainActivity.sendMessageToServer(message);
    }


}
