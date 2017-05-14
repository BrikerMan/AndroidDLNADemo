package com.example.brikerman.dlnademo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.eliyar.bfdlna.DLNADeviceScanListener;
import com.eliyar.bfdlna.DLNAManager;
import com.eliyar.bfdlna.SSDP.Device;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DLNADeviceScanListener {

    private DLNAManager manager;
    private ListView mListView;

    private ArrayList<Device> devices = new ArrayList<Device>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.device_list_view);
        findViewById(R.id.btnSearch).setOnClickListener(this);

        manager = new DLNAManager((WifiManager) getSystemService(WIFI_SERVICE));
        manager.setScanDeviceListener(this);
        try {
            manager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DeviceListAdapter adapter = new DeviceListAdapter(this, devices);
        mListView.setAdapter(adapter);

//        final Context context = this;
    }

    @Override
    public void onClick(View v) {
        manager.refreshDevices();
    }


    private void startUDPServer() {

    }

    @Override
    public void didFoundDevice(Device device) {
        this.devices = manager.devices;

        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DeviceListAdapter adapter = new DeviceListAdapter(context, devices);
                mListView.setAdapter(adapter);

            }
        });

    }
}
