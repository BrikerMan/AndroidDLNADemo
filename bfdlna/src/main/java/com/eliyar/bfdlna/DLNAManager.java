package com.eliyar.bfdlna;


import java.net.InetAddress;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.eliyar.bfdlna.Exceptions.WifiDisableException;
import com.eliyar.bfdlna.SSDP.Device;
import com.eliyar.bfdlna.SSDP.ServiceType;
//import com.eliyar.bfdlna.Exceptions.WifiDisableException;


import static android.content.Context.WIFI_SERVICE;

/**
 * Created by brikerman on 2017/5/13.
 */

public class DLNAManager implements UdpReceiveListener {
    private static final String TAG = "DLNA DLNAManager";

    private UdpHelper mUdpHelper;

    private ServiceType targetType;
    private WifiManager wifiManager;

    public DLNAManager(WifiManager manager) {
        wifiManager = manager;
        targetType = ServiceType.UPnP_AVTransport1;
    }

    public void start() throws Exception {
        mUdpHelper = new UdpHelper(wifiManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    mUdpHelper.stopListen();
                    mUdpHelper.startListen(DLNAManager.this);
                    fireSearchRequest();
                } catch (WifiDisableException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void refreshDevices() {
        fireSearchRequest();
    }

    private void fireSearchRequest() {
        String message = "M-SEARCH * HTTP/1.1\r\n" +
                "HOST: 239.255.255.250:1900\r\n" +
                "MAN: \"ssdp:discover\"\r\n" +
                "ST: urn:schemas-upnp-org:service:AVTransport:1\r\n" +
                "MX: 3\r\n" +
                "USER-AGENT: UPnP/1.0 FengmiDLNA/fengmi/NewDLNA/1.0";
        mUdpHelper.sendMulticast(message);
    }

    @Override
    public void receive(InetAddress address, String msg) {
        Device device = new Device(msg);
        Log.v(TAG, device.toString());
    }
}
