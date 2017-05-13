package com.eliyar.bfdlna.SSDP;

import android.util.Log;

import java.net.URL;

/**
 * Created by brikerman on 2017/5/13.
 */

public class Device {
    private static final String TAG = "DLNA SSDP Device";

    /// URL链接
    URL location = null;

    /// 服务器IP
    String host = null;

    /// 服务基础地址
    String baseURL = null;

    public Device(String msg) {
        String lines[] = msg.split("\\r?\\n");
        for(String line : lines ) {
            Log.v("Device", line);
            String parse[] = line.split(": ");
            if (parse[0] != null) {
                switch (parse[0].toUpperCase()){
                    case "LOCATION":
                        if (parse[1].toString() != null) {
                            try {
                                URL loc = new URL(parse[1].toString());
                                location = loc;
                                host     = location.getHost();
                                baseURL  = location.getPath();
                            } catch (Exception e) {
                                Log.e(TAG, "location parse error" + parse[1]);
                                e.printStackTrace();
                            }
                        }
                    default:
                        break;
                }
            }
        }
    }

    public void startParseXML(final DeviceInfoListener listener) throws Exception {

    }

    @Override
    public String toString() {
        if (location == null) {
            return "==========SSDP Null Device=========";
        } else {
            return "==========SSDPDevice=========\n" +
                    "Location: " + location.toString();
        }
    }
}
