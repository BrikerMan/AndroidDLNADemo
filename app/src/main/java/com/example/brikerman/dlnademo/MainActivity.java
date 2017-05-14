package com.example.brikerman.dlnademo;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.eliyar.bfdlna.DLNADeviceScanListener;
import com.eliyar.bfdlna.DLNAManager;
import com.eliyar.bfdlna.SSDP.SSDPDevice;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DLNADeviceScanListener {

    private DLNAManager manager;
    private ListView mListView;

//    private ArrayList<SSDPDevice> devices = new ArrayList<SSDPDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.device_list_view);
        findViewById(R.id.btnSearch).setOnClickListener(this);

        // 初始化 DLNA 单例
        manager = DLNAManager.getInstance();
        manager.start();
        manager.setScanDeviceListener(this);

        final DeviceListAdapter adapter = new DeviceListAdapter(this, manager.devices);
        mListView.setAdapter(adapter);

        final Context context = this;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SSDPDevice device= manager.devices.get(position);
                // 2
                Intent detailIntent = new Intent(context, ControlActicy.class);
                // 3
                detailIntent.putExtra("device", device);
                // 4
                startActivity(detailIntent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        manager.refreshDevices();
    }


    private void startUDPServer() {

    }

    @Override
    public void didFoundDevice(SSDPDevice SSDPDevice) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DeviceListAdapter adapter = new DeviceListAdapter(context, manager.devices);
                mListView.setAdapter(adapter);
            }
        });

    }
}
