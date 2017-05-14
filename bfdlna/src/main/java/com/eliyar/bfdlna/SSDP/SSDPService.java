package com.eliyar.bfdlna.SSDP;

import com.eliyar.bfdlna.XMLParser;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by brikerman on 2017/5/13.
 */

public class SSDPService implements Serializable {
    /// 服务类型
    public SSDPServiceType serviceType = null;
    /// 服务表示符
    public String serviceId = null;
    /// 获取服务描述文档URL
    public URL SCPDURL = null;
    /// 发出控制消息URL
    public URL controlURL = null;
    /// 订阅该服务事件URL
    public URL eventSubURL = null;

    public SSDPService(Element node, String base) {
        XMLParser parser = new XMLParser();

        String typeString = parser.getValue(node, "serviceType");
        if (typeString != null) {
            serviceType = SSDPServiceType.getWithString(typeString);
        }

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
                "      serviceId       " + this.serviceId   + "\n" +
                "      SSDPServiceType " + this.serviceType + "\n" +
                "      SCPDURL         " + this.SCPDURL     + "\n" +
                "      controlURL      " + this.controlURL  + "\n" +
                "      eventSubURL     " + this.eventSubURL + "\n";
    }
}
