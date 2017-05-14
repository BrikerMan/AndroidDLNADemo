package com.eliyar.bfdlna.SSDP;

import android.util.Log;

import com.eliyar.bfdlna.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by brikerman on 2017/5/13.
 */

public class SSDPDevice implements Serializable {
    private static final String TAG = "DLNA SSDP SSDPDevice";

    /**
     * 服务描述文件 URL
     */
    public URL location = null;

    /**
     * 服务器 IP
     */
    String host = null;

    /**
     * 服务基础地址
     */
    public String baseURL = null;

    public String uuid = "";


    public String friendlyName = "";

    /**
     * 原始 SSDP 字典
     */
    public HashMap<String, String> info = new HashMap<String, String>();

    public List<SSDPService> services = new ArrayList<SSDPService>();

    public Boolean isValid() {
        return location != null;
    }

    public SSDPDevice() { }

    public SSDPDevice(String msg) {
        String lines[] = msg.split("\\r?\\n");
        for(String line : lines ) {
            String parse[] = line.split(": ");
            if (parse[0] != null) {
                String key = parse[0].toUpperCase();
                if (parse.length > 1) {
                    if (parse[1].toString() != null) {
                        info.put(key, parse[1].toString());
                    }
                }
                switch (key){
                    case "LOCATION":
                        if (parse[1].toString() != null) {
                            try {
                                URL loc = new URL(parse[1].toString());
                                location = loc;
                                host     = String.format("%s://%s/", location.getProtocol(), location.getHost());
                                baseURL  = String.format("%s://%s:%s/", location.getProtocol(), location.getHost(), location.getPort());
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

    public void startParseXML(final SSDPDeviceInfoListener listener) throws Exception {
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(location)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String xml = response.body().string();
                XMLParser parser = new XMLParser();
                Document doc = parser.getDomElement(xml);

                NodeList devicesList = doc.getElementsByTagName("device");

                Element device = (Element) devicesList.item(0);
                if (device != null) {
                    friendlyName = parser.getValue(device, "friendlyName");
                    uuid = parser.getValue(device, "UDN");
                    NodeList servicesList = device.getElementsByTagName("service");
                    for (int i = 0; i < servicesList.getLength(); i++) {
                        SSDPService service = new SSDPService((Element) servicesList.item(i), baseURL);
                        services.add(service);
                    }
                }
                if (!services.isEmpty()) {
                    listener.finishParseServices(SSDPDevice.this);
                }
            }
        });
    }

//    public static void main(String [] args) {
//        SSDPDevice d = new SSDPDevice();
//        try {
//            FileOutputStream fileOut = new FileOutputStream("/tmp/ssdpDevice.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(d);
//            out.close();
//            fileOut.close();
//            System.out.printf("Serialized data is saved in /tmp/employee.ser");
//        } catch(IOException i) {
//            i.printStackTrace();
//        }
//    }



    @Override
    public String toString() {
        if (location == null) {
            return "==========SSDP Null SSDPDevice=========";
        } else {
            String decs = "==========SSDPDevice=========" + "\n" +
                    "friendlyName :" + this.friendlyName + "\n" +
                    "location     :" + this.location + "\n" +
//                    "version      :" + this.version + "\n" +
//                    "uuid         :" + this.uuid + "\n" +

                    "services:";

            for (SSDPService ser: services) {
                decs = decs + ser.toString();
            }

            return decs;
        }
    }
}
