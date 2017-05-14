package com.eliyar.bfdlna.Services;

import android.util.Log;

import com.eliyar.bfdlna.DLNARequestCallBack;
import com.eliyar.bfdlna.SSDP.SSDPDevice;
import com.eliyar.bfdlna.SSDP.SSDPService;
import com.eliyar.bfdlna.SSDP.SSDPServiceType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    public void fireReques(String xml, String action) {
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
                Log.v("response ", "onResponse(): " + response.body().string());
                if (callBack != null) {
                    HashMap<String, String> info = new HashMap<String, String>();
                    callBack.onSuccess(info);
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