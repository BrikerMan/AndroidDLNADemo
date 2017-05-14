package com.eliyar.bfdlna;


import java.net.InetAddress;
import java.util.ArrayList;

import android.net.wifi.WifiManager;
import android.util.Log;

import com.eliyar.bfdlna.Exceptions.WifiDisableException;
import com.eliyar.bfdlna.SSDP.Device;
import com.eliyar.bfdlna.SSDP.DeviceInfoListener;
import com.eliyar.bfdlna.SSDP.ServiceType;
//import com.eliyar.bfdlna.Exceptions.WifiDisableException;


import static android.content.Context.WIFI_SERVICE;

/**
 * Created by brikerman on 2017/5/13.
 */

public class DLNAManager implements UdpReceiveListener, DeviceInfoListener {
    private static final String TAG = "DLNA DLNAManager";

    public ArrayList<Device> devices = new ArrayList<Device>();
    public ArrayList<String> devicesURLs = new ArrayList<String>();
    private UdpHelper mUdpHelper;

    private ServiceType targetType;
    private WifiManager wifiManager;

    private DLNADeviceScanListener scanDeviceListener;

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

    public void setScanDeviceListener(DLNADeviceScanListener listener) {
        scanDeviceListener = listener;
    }

    public void refreshDevices() {
        devicesURLs.clear();
        devices.clear();
        fireSearchRequest();
    }

    private void fireSearchRequest() {
//        String message = "M-SEARCH * HTTP/1.1\r\n" +
//                "HOST: 239.255.255.250:1900\r\n" +
//                "MAN: \"ssdp:discover\"\r\n" +
//                "ST: ssdp:all\r\n" +
//                "MX: 3\r\n" +
//                "USER-AGENT: UPnP/1.0 FengmiDLNA/fengmi/NewDLNA/1.0";
        String message = "M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:1900\r\nMAN: \"ssdp:discover\"\r\nST: urn:schemas-upnp-org:service:AVTransport:1\r\nMX: 3\r\nUSER-AGENT: /1\\\n\r\n\r\n";
        mUdpHelper.sendMulticast(message);
    }

    @Override
    public void receive(InetAddress address, String msg) {
        Log.v(TAG, msg);
        Device device = new Device(msg);
        if (device.isValid() && !devices.contains(device)) {
            try {
                device.startParseXML(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Log.v(TAG, device.toString());
    }

    @Override
    public void finishParseServices(Device device) {
        if (!devicesURLs.contains(device.baseURL)) {
            devicesURLs.add(device.baseURL);
            devices.add(device);
            if (scanDeviceListener != null) {
                scanDeviceListener.didFoundDevice(device);
            }
            Log.v(TAG, device.toString());
        }
    }
}
