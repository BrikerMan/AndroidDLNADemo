package com.eliyar.bfdlna;


import java.net.InetAddress;
import java.util.ArrayList;

import android.net.wifi.WifiManager;
import android.util.Log;

import com.eliyar.bfdlna.SSDP.SSDPDevice;
import com.eliyar.bfdlna.SSDP.SSDPDeviceInfoListener;
import com.eliyar.bfdlna.SSDP.SSDPServiceType;
import com.eliyar.bfdlna.Services.AVTransportManager;


/**
 * Created by brikerman on 2017/5/13.
 */

public class DLNAManager implements UdpReceiveListener, SSDPDeviceInfoListener {
    private static final String TAG = "DLNA DLNAManager";

    public ArrayList<SSDPDevice> devices = new ArrayList<SSDPDevice>();
    public ArrayList<String> devicesURLs = new ArrayList<String>();
    public SSDPDevice mCuurentDevice;

    private UdpHelper mUdpHelper;
    private SSDPServiceType targetType = SSDPServiceType.UPnP_AVTransport1;
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
     * 服务启动，启动后自动开始搜索
     * @throws Exception
     */
    public void start() {
        mUdpHelper = new UdpHelper();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mUdpHelper.stopListen();
                mUdpHelper.startListen(DLNAManager.this);
                fireSearchRequest();
            }
        }).start();
    }

    /**
     * 建议网络环境发生变化后调用
     */
    public void reset() {
        mUdpHelper.reset();
        refreshDevices();
    }

    /**
     * 设置设备搜索结果回调
     * @param listener 回调接口
     */
    public void setScanDeviceListener(DLNADeviceScanListener listener) {
        scanDeviceListener = listener;
    }

    /**
     * 选中设备，用于后续操作
     * @param device 设备
     */
    public void setCurrentDevice(SSDPDevice device) {
        mCuurentDevice = device;
    }

    /**
     * 获取媒体控制类实例，用于播放暂停投屏等操作
     * @return AVTransportManager 实例
     */
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
