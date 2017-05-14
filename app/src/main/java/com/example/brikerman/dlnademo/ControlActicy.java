package com.example.brikerman.dlnademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eliyar.bfdlna.DLNAManager;
import com.eliyar.bfdlna.DLNARequestCallBack;
import com.eliyar.bfdlna.SSDP.SSDPDevice;
import com.eliyar.bfdlna.Services.AVTransportManager;

import java.util.HashMap;

public class ControlActicy extends AppCompatActivity implements View.OnClickListener {

    SSDPDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_acticy);

        Intent intent = getIntent();
        device = (SSDPDevice) intent.getSerializableExtra("device");
        DLNAManager.getInstance().setCurrentDevice(device);
        findViewById(R.id.btnSetURI).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        AVTransportManager manager = DLNAManager.getInstance().getAVTransportManager();
        manager.setCallBack(new DLNARequestCallBack() {
            @Override
            public void onFailure() {
                Log.e("Failed", "ffff");
            }

            @Override
            public void onSuccess(HashMap<String,String> info) {

            }
        });
        manager.SetAVTransportURI("http://baobab.wdjcdn.com/1455968234865481297704.mp4");

//        try {
//            DLNAManager.getInstance()
//                    .avTransport
//                    .SetAVTransportURI("http://baobab.wdjcdn.com/1455968234865481297704.mp4", "", "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
