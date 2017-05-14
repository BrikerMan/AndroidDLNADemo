package com.eliyar.bfdlna.Services;

import android.util.Log;

import com.eliyar.bfdlna.DLNARequestCallBack;
import com.eliyar.bfdlna.SSDP.SSDPDevice;
import com.eliyar.bfdlna.SSDP.SSDPService;
import com.eliyar.bfdlna.SSDP.SSDPServiceType;
import com.eliyar.bfdlna.Utils;
import com.eliyar.bfdlna.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by brikerman on 2017/5/14.
 */

public class AVTransportManager {

    public static String TRACK_DURATION = "TrackDuration";
    public static String ABS_TIME = "AbsTime";
    public static String REL_TIME = "RelTime";


    public SSDPService service;

    private DLNARequestCallBack callBack;

    public AVTransportManager(SSDPDevice device) {
        for (SSDPService service: device.services) {
            if (service.serviceType == SSDPServiceType.UPnP_AVTransport1) {
                this.service = service;
            }
        }
    }

    public void setCallBack(DLNARequestCallBack callBack) {
        this.callBack = callBack;
    }

    public void SetAVTransportURI(String uri) {
        SetAVTransportURI(uri, "", "");
    }

    public void SetAVTransportURI(String uri, String title, String meta) {
        Log.d("AVTransportManager", "SetAVTransportURI");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:SetAVTransportURI");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");

            Element child2 = doc.createElement("CurrentURI");
            child2.setTextContent(uri);

            Element child3 = doc.createElement("CurrentURIMetaData");
            child3.setTextContent(meta);

            command.appendChild(child1);
            command.appendChild(child2);
            command.appendChild(child3);

            String xml = prepareXML(command);
            fireReques(xml, "SetAVTransportURI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        Log.d("AVTransportManager", "play");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:Play");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");

            Element child2 = doc.createElement("Speed");
            child2.setTextContent("1");

            command.appendChild(child1);
            command.appendChild(child2);

            String xml = prepareXML(command);
            fireReques(xml, "Play");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        Log.d("AVTransportManager", "pause");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:Pause");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");
            command.appendChild(child1);

            String xml = prepareXML(command);
            fireReques(xml, "Pause");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        Log.d("AVTransportManager", "stop");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:Stop");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");
            command.appendChild(child1);

            String xml = prepareXML(command);
            fireReques(xml, "Stop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 快进操作
     * @param target 目标时间，单位秒
     */
    public void seek(Integer target) {
        Log.d("AVTransportManager", "Seek");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:Seek");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");
            command.appendChild(child1);

            Element child2 = doc.createElement("Unit");
            child2.setTextContent("REL_TIME");
            command.appendChild(child2);

            String t = Utils.convertSecondToString(target);

            Element child3 = doc.createElement("Target");
            child3.setTextContent(t);
            command.appendChild(child3);

            String xml = prepareXML(command);
            fireReques(xml, "Seek");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPositionInfo() {
        Log.d("AVTransportManager", "GetPositionInfo");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:GetPositionInfo");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");
            command.appendChild(child1);

            Element child2 = doc.createElement("MediaDuration");
            command.appendChild(child2);

            String xml = prepareXML(command);
            fireReques(xml, "GetPositionInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTransportInfo() {
        Log.d("AVTransportManager", "GetTransportInfo");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element command = doc.createElement("u:GetTransportInfo");
            command.setAttribute("xmlns:u", "urn:schemas-upnp-org:service:AVTransport:1");

            Element child1 = doc.createElement("InstanceID");
            child1.setTextContent("0");
            command.appendChild(child1);

            String xml = prepareXML(command);
            fireReques(xml, "GetTransportInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fireReques(String xml, String action) {
        Log.d("AVTransportManager", "fireReques" + xml);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/xml");
        RequestBody body = RequestBody.create(mediaType, xml);

        Request request = new Request.Builder()
                .url(service.controlURL)
                .post(body)
                .addHeader("content-type", "application/xml")
                .addHeader("soapaction", service.serviceType.decs + "#" + action)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: ");
                if (callBack != null) {
                    callBack.onFailure();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String xml = response.body().string();
                Log.v("response ", "onResponse(): " + xml);


                XMLParser parser = new XMLParser();
                Document doc = parser.getDomElement(xml);

                Boolean isSuccess = false;
                HashMap<String, String> info = new HashMap<String, String>();

                if (doc != null) {
                    NodeList responeBody = doc.getElementsByTagName("s:Body");
                    Element body = (Element) responeBody.item(0);

                    if (body != null) {
                        Element child = (Element) body.getFirstChild();
                        switch (child.getNodeName()) {
                            case "u:SetAVTransportURIResponse":
                                play();
                                isSuccess = true;
                                break;

                            case "u:GetPositionInfoResponse":
                                String realTime = parser.getValue(child, REL_TIME);
                                String absTime  = parser.getValue(child, ABS_TIME);
                                String trackDuration = parser.getValue(child, TRACK_DURATION);

                                info.put(REL_TIME, realTime);
                                info.put(ABS_TIME , absTime);
                                info.put(TRACK_DURATION, trackDuration);

                                isSuccess = true;
                                break;

                            case "u:GetTransportInfoResponse":
                                String state = parser.getValue(child, "CurrentTransportState");
                                info.put("State", state);
                                isSuccess = true;
                                break;

                            case "u:PlayResponse":
                                isSuccess = true;
                                break;


                            case "u:PauseResponse":
                                isSuccess = true;
                                break;


                            case "u:StopResponse":
                                isSuccess = true;
                                break;

                            case "u:SeekResponse":
                                isSuccess = true;
                                break;

                            default :
                                Log.e("onResponse", "未定义响应:" + xml);
                        }
                    }
                }

                if (callBack != null) {
                    if (isSuccess) {
                        callBack.onSuccess(info);
                    } else {
                        callBack.onFailure();
                    }
                }
            }
        });
    }

    public String prepareXML(final Element command) {
        String envelopeTop = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>";
        String envelopeEnd = "</s:Body></s:Envelope>";

        return envelopeTop + getNodeString(command) + envelopeEnd;
    }

    String getNodeString(Node node) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            String output = writer.toString();
            return output.substring(output.indexOf("?>") + 2);//remove <?xml version="1.0" encoding="UTF-8"?>
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return node.getTextContent();
    }
}