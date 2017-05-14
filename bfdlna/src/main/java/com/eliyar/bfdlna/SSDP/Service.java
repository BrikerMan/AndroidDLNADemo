package com.eliyar.bfdlna.SSDP;

import android.util.Log;

import com.eliyar.bfdlna.XMLParser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.net.URL;

/**
 * Created by brikerman on 2017/5/13.
 */

public class Service {
    /// 服务类型
    ServiceType serviceType = null;
    /// 服务表示符
    String serviceId = null;
    /// 获取服务描述文档URL
    URL SCPDURL = null;
    /// 发出控制消息URL
    URL controlURL = null;
    /// 订阅该服务事件URL
    URL eventSubURL = null;

    public Service(Element node, String base) {
        XMLParser parser = new XMLParser();

        String typeString = parser.getValue(node, "serviceType");
//        serviceType = ServiceType.valueOf(typeString);

        serviceId = parser.getValue(node, "serviceId");

        String scp = parser.getValue(node, "SCPDURL");
        if (scp.startsWith("/")) {
            scp = scp.substring(1);
        }

        String control = parser.getValue(node, "controlURL");
        if (control.startsWith("/")) {
            control = control.substring(1);
        }

        String event = parser.getValue(node, "eventSubURL");
        if (event.startsWith("/")) {
            event = event.substring(1);
        }

        try {
            SCPDURL     = new URL(base + scp);
            controlURL  = new URL(base + control);
            eventSubURL = new URL(base + event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "---------\n"		+
                "      serviceId      " + this.serviceId   + "\n" +
                "      serviceType    " + this.serviceType + "\n" +
                "      SCPDURL        " + this.SCPDURL     + "\n" +
                "      controlURL     " + this.controlURL  + "\n" +
                "      eventSubURL    " + this.eventSubURL + "\n";
    }
}
