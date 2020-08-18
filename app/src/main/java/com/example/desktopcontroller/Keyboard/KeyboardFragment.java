package com.example.desktopcontroller.Keyboard;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.desktopcontroller.MainActivity;
import com.example.desktopcontroller.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class KeyboardFragment extends Fragment implements TextWatcher, View.OnTouchListener, View.OnClickListener {

    EditText keyEditText;
    Button altButton,escButton,tabButton,shiftButton,enterButton,backspaceButton,clearButton,ctrlButton;
    CharSequence previousText="";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard,container,false);

        keyEditText = view.findViewById(R.id.keyeditText);
        clearButton = view.findViewById(R.id.ClearButton);
        altButton = view.findViewById(R.id.AltButton);
        escButton = view.findViewById(R.id.EscButton);
        tabButton = view.findViewById(R.id.TabButton);
        shiftButton = view.findViewById(R.id.ShiftButton);
        enterButton = view.findViewById(R.id.EnterButton);
        backspaceButton = view.findViewById(R.id.BackspaceButton);
        ctrlButton = view.findViewById(R.id.CtrlButton);

        //open the soft keyboard
        keyEditText.requestFocus();
        InputMethodManager keyboard=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        keyEditText.addTextChangedListener(this);

        escButton.setOnClickListener(this);
        tabButton.setOnClickListener(this);
        backspaceButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        enterButton.setOnClickListener(this);

        shiftButton.setOnTouchListener(this);
        ctrlButton.setOnTouchListener(this);
        altButton.setOnTouchListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ClearButton) {
            keyEditText.setText("");
        }else {
            int keyCode = 17;//initialization
            String action = "TYPE_KEY";
            switch (id) {
                case R.id.EnterButton:
                    keyCode = 10;
                    break;
                case R.id.TabButton:
                    keyCode = 9;
                    break;
                case R.id.EscButton:
                    keyCode = 27;
                    break;
                case R.id.BackspaceButton:
                    keyCode = 8;
                    break;
            }
            Log.i("key", "onTouch: "+keyCode);

            //send key code to server
            sendKeyCodeToServer(action, keyCode);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String action = "KEY_PRESS";
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            action = "KEY_PRESS";
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            action = "KEY_RELEASE";
        }
        int keyCode = 17;//initialization
        switch (v.getId()) {

            case R.id.CtrlButton:
                keyCode = 17;
                break;
            case R.id.AltButton:
                keyCode = 18;
                break;
            case R.id.ShiftButton:
                keyCode = 16;
                break;
        }
        Log.i("key", "onTouch: "+keyCode);

        //send key code to server
        sendKeyCodeToServer(action, keyCode);
        return false;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        char ch = newCharacter(s, previousText); //get the new characters
        Log.i("text", "onTextChanged: "+ch);
        if (ch == 0) {
            return;
        }

        //send data to server
        MainActivity.sendMessageToServer("TYPE_CHARACTER");
        MainActivity.sendMessageToServer(Character.toString(ch));
        previousText = s.toString();

    }

    private void sendKeyCodeToServer(String action, int keyCode) {
        MainActivity.sendMessageToServer(action);
        MainActivity.sendMessageToServer(keyCode);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private char newCharacter(CharSequence currentText, CharSequence previousText) {
        char ch = 0;
        int currentTextLength = currentText.length();
        int previousTextLength = previousText.length();
        int difference = currentTextLength - previousTextLength;
        if (currentTextLength > previousTextLength) {
            if (1 == difference) {
                ch = currentText.charAt(previousTextLength);
            }
        } else if (currentTextLength < previousTextLength) {
            if (-1 == difference) {
                ch = '\b';
            } else {
                ch = ' ';
            }
        }
        Log.i("Text", "newCharacter: "+currentText+" "+previousText);
        return ch;
    }



}
