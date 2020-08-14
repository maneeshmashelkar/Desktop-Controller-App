package com.example.desktopcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.desktopcontroller.Connect.ConnectFragment;
import com.example.desktopcontroller.Keyboard.KeyboardFragment;
import com.example.desktopcontroller.Mouse.MouseFragment;
import com.example.desktopcontroller.PowerOff.PowerOffFragment;
import com.example.desktopcontroller.Presentation.PresentationFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    TextView toolText;
    public static Socket clientSocket = null;
    public static ObjectOutputStream objectOutputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolText = toolbar.findViewById(R.id.toolbar_title);
        toolText.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        //load default fragment

        replaceFragment(R.id.connect);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        replaceFragment(id);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.connect:
                fragment = new ConnectFragment();

                toolText.setText(R.string.connect);
                break;
            case R.id.mouse:
                fragment = new MouseFragment();

                toolText.setText(R.string.mouse);
                break;
            case R.id.keyboard:
                fragment = new KeyboardFragment();

                toolText.setText(R.string.keyboard);
                break;
            case R.id.presentation:
                fragment = new PresentationFragment();

                toolText.setText(R.string.presentation);
                break;
            case R.id.poweroff:
                fragment = new PowerOffFragment();
                toolText.setText(R.string.poweroff);
                break;

        }
            if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, fragment);
            fragmentTransaction.commit();
        }
    }



    protected void onDestroy() {
        super.onDestroy();
        try {
            if (MainActivity.clientSocket != null) {
                MainActivity.clientSocket.close();
            }
            if (MainActivity.objectOutputStream != null) {
                MainActivity.objectOutputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageToServer(String message) {
        if (MainActivity.clientSocket != null) {
            new SendMessageToServer().execute(String.valueOf(message), "STRING");
        }
    }

    public static void sendMessageToServer(int message) {
        if (MainActivity.clientSocket != null) {
            new SendMessageToServer().execute(String.valueOf(message), "INT");
        }
    }

}
