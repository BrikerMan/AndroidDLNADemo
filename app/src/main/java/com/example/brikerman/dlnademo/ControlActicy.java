package com.example.brikerman.dlnademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eliyar.bfdlna.DLNAManager;
import com.eliyar.bfdlna.DLNARequestCallBack;
import com.eliyar.bfdlna.SSDP.SSDPDevice;
import com.eliyar.bfdlna.Services.AVTransportManager;
import com.eliyar.bfdlna.Utils;

import java.util.HashMap;

public class ControlActicy extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    SSDPDevice device;

    TextView currentTimeLabel;
    TextView totalDurationlabel;

    Integer totalDuration = 0;

    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_acticy);

        Intent intent = getIntent();
        device = (SSDPDevice) intent.getSerializableExtra("device");
        DLNAManager.getInstance().setCurrentDevice(device);
        findViewById(R.id.btnSetURI).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
        findViewById(R.id.btnPlay).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.btnGetPosition).setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        currentTimeLabel   = (TextView) findViewById(R.id.left_Textview);
        totalDurationlabel = (TextView) findViewById(R.id.right_Textview);

        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetURI:
                AVTransportManager manager = DLNAManager.getInstance().getAVTransportManager();
                /**
                 * 如果需要关注结果，则需要实现协议；
                 */
                manager.setCallBack(new DLNARequestCallBack() {
                    @Override
                    public void onFailure() {
                        Log.e("ControlActicy", "onFailure");
                    }

                    @Override
                    public void onSuccess(HashMap<String,String> info) {
                        Log.d("ControlActicy", "onSuccess");
                    }
                });
                manager.SetAVTransportURI("http://baobab.wdjcdn.com/1455968234865481297704.mp4");
                break;

            case R.id.btnPause:
                AVTransportManager manager1 = DLNAManager.getInstance().getAVTransportManager();
                manager1.pause();
                break;

            case R.id.btnStop:
                AVTransportManager manager2 = DLNAManager.getInstance().getAVTransportManager();
                manager2.stop();
                break;

            case R.id.btnPlay:
                AVTransportManager manager3 = DLNAManager.getInstance().getAVTransportManager();
                manager3.play();
                break;

            case R.id.btnGetPosition:
                AVTransportManager manager4 = DLNAManager.getInstance().getAVTransportManager();
                manager4.setCallBack(new DLNARequestCallBack() {
                    @Override
                    public void onFailure() {
                        Log.e("ControlActicy", "onFailure");
                    }

                    @Override
                    public void onSuccess(final HashMap<String,String> info) {
                        Log.d("ControlActicy", info.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentTimeLabel.setText(info.get(AVTransportManager.REL_TIME));
                                totalDurationlabel.setText(info.get(AVTransportManager.TRACK_DURATION));

                                Integer realTime = Utils.convertStringToSecond(info.get(AVTransportManager.REL_TIME));
                                Integer duration = Utils.convertStringToSecond(info.get(AVTransportManager.TRACK_DURATION));
                                totalDuration = duration;
                                int progress = (int) ((double) realTime / (double) duration * 100);
                                Log.v("tttt", " " + progress);

                                seekBar.setProgress(progress);
                            }
                        });
                    }
                });
                manager4.getPositionInfo();
                break;

            default:
                break;
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
//        Log.v("ddd", "" + seekBar.getProgress());

    }

    public void onStartTrackingTouch(SeekBar seekBar) {}

    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.v("ddd", "" + seekBar.getProgress());
        if (totalDuration != 0) {
            Integer target = (int) ((double) seekBar.getProgress() / 100 * (double) totalDuration);
            AVTransportManager manager = DLNAManager.getInstance().getAVTransportManager();
            manager.seek(target);
        }
     }
}
