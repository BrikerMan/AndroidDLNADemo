package com.eliyar.bfdlna;


import java.net.InetAddress;
import java.util.ArrayList;

import android.net.wifi.WifiManager;
import android.util.Log;

import com.eliyar.bfdlna.Exceptions.WifiDisableException;
import com.eliyar.bfdlna.SSDP.SSDPDevice;
import com.eliyar.bfdlna.SSDP.SSDPDeviceInfoListener;
import com.eliyar.bfdlna.SSDP.SSDPService;
import com.eliyar.bfdlna.SSDP.SSDPServiceType;
import com.eliyar.bfdlna.Services.AVTransportManager;


/**
 * Created by brikerman on 2017/5/13.
 */

public class DLNAManager implements UdpReceiveListener, SSDPDeviceInfoListener {
    private static final String TAG = "DLNA DLNAManager";

    public ArrayList<SSDPDevice> devices = new ArrayList<SSDPDevice>();
    public ArrayList<String> devicesURLs = new ArrayList<String>();

    public AVTransportManager avTransport;

    public SSDPDevice mCuurentDevice;

    private UdpHelper mUdpHelper;

    private SSDPServiceType targetType = SSDPServiceType.UPnP_AVTransport1;
    private WifiManager wifiManager;

    private DLNADeviceScanListener scanDeviceListener;

    /**
     * Singleton
     */
    private static DLNAManager instance;
    private DLNAManager (){}

    public static DLNAManager getInstance() {
        if (instance == null) {
            instance = new DLNAManager();
        }
        return instance;
    }

    /**
     * 服务器启动
     * @param manager    网络控制器，用于检测网络环境
     * @throws Exception
     */
    public void start(WifiManager manager) throws Exception {
        wifiManager = manager;
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

    public void setCurrentDevice(SSDPDevice device) {
        mCuurentDevice = device;
    }

    public AVTransportManager getAVTransportManager() {
        if (mCuurentDevice != null) {
            return new AVTransportManager(mCuurentDevice);
        } else {
            return null;
        }
    }

    /**
     * 刷新设备列表
     */
    public void refreshDevices() {
        devicesURLs.clear();
        devices.clear();
        fireSearchRequest();
    }

    private void fireSearchRequest() {
        String message = "M-SEARCH * HTTP/1.1\r\n" +
                "HOST: 239.255.255.250:1900\r\n" +
                "MAN: \"ssdp:discover\"\r\n" +
                "ST: " + targetType.decs + "\r\n" +
                "MX: 3\r\n" +
                "USER-AGENT: UPnP/1.0 FengmiDLNA/fengmi/NewDLNA/1.0\r\n\r\n\r\n\n";

        mUdpHelper.sendMulticast(message);
    }

    @Override
    public void udpReceive(InetAddress address, String msg) {
        Log.v(TAG, msg);
        SSDPDevice SSDPDevice = new SSDPDevice(msg);
        if (SSDPDevice.isValid() && !devices.contains(SSDPDevice)) {
            try {
                SSDPDevice.startParseXML(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finishParseServices(SSDPDevice SSDPDevice) {
        if (!devicesURLs.contains(SSDPDevice.baseURL)) {
            devicesURLs.add(SSDPDevice.baseURL);
            devices.add(SSDPDevice);
            if (scanDeviceListener != null) {
                scanDeviceListener.didFoundDevice(SSDPDevice);
            }
            Log.v(TAG, SSDPDevice.toString());
        }
    }
}
