package com.example.brikerman.dlnademo;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.eliyar.bfdlna.DLNAManager;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DLNAManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSearch).setOnClickListener(this);

        manager = new DLNAManager((WifiManager) getSystemService(WIFI_SERVICE));
        try {
            manager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        manager.refreshDevices();
    }


    private void startUDPServer() {

    }
}
