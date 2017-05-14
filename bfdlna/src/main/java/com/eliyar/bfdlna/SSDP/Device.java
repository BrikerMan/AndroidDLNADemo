package com.eliyar.bfdlna.SSDP;

import android.util.Log;

import com.eliyar.bfdlna.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by brikerman on 2017/5/13.
 */

public class Device {
    private static final String TAG = "DLNA SSDP Device";

    /// URL链接
    public URL location = null;

    /// 服务器IP
    String host = null;

    /// 服务基础地址
    public String baseURL = null;

    public String uuid = "";
    public String friendlyName = "";

    List<Service> services = new ArrayList<Service>();

    public Boolean isValid() {
        return location != null;
    }

    public Device(String msg) {
        String lines[] = msg.split("\\r?\\n");
        for(String line : lines ) {
            String parse[] = line.split(": ");
            if (parse[0] != null) {
                switch (parse[0].toUpperCase()){
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


    public void startParseXML(final DeviceInfoListener listener) throws Exception {
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
                        Service service = new Service((Element) servicesList.item(i), baseURL);
                        services.add(service);
                    }
                }
                if (!services.isEmpty()) {
                    listener.finishParseServices(Device.this);
                }
            }
        });
    }

//    private String parseBaseLocaiton() {
//        if (location != null) {
//            URL url = location;
//            String proto = url.getProtocol();
//            String host = url.getHost();
//            int port = url.getPort();
//
//            // if the port is not explicitly specified in the input, it will be -1.
//            if (port != -1) {
//                return String.format("%s://%s", proto, host);
//            } else {
//                return String.format("%s://%s:%d", proto, host, port);
//            }
//        }
//    }

    @Override
    public String toString() {
        if (location == null) {
            return "==========SSDP Null Device=========";
        } else {
            String decs = "==========SSDPDevice=========" + "\n" +
                    "location     :" + this.location + "\n" +
//                    "version      :" + this.version + "\n" +
//                    "uuid         :" + this.uuid + "\n" +
//                    "friendlyName :" + this.friendlyName + "\n" +
                    "services:";

            for (Service ser: services) {
                decs = decs + ser.toString();
            }

            return decs;
        }
    }
}
