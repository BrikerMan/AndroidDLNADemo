package com.eliyar.bfdlna.SSDP;

import java.io.Serializable;

/**
 * Created by brikerman on 2017/5/13.
 */

public enum SSDPServiceType implements Serializable {
    All("ssdp:all"),
    UPnP_RootDevice("upnp:rootdevice"),

    // UPnP Internet Gateway SSDPDevice (IGD)
    UPnP_InternetGatewayDevice1("urn:schemas-upnp-org:device:InternetGatewayDevice:1"),
    UPnP_WANConnectionDevice1("urn:schemas-upnp-org:device:WANConnectionDevice:1"),
    UPnP_WANDevice1("urn:schemas-upnp-org:device:WANDevice:1"),
    UPnP_WANCommonInterfaceConfig1("urn:schemas-upnp-org:service:WANCommonInterfaceConfig:1"),
    UPnP_WANIPConnection1("urn:schemas-upnp-org:service:WANIPConnection:1"),
    UPnP_Layer3Forwarding1("urn:schemas-upnp-org:service:Layer3Forwarding:1"),

    // UPnP A/V profile
    UPnP_MediaServer1("urn:schemas-upnp-org:device:MediaServer:1"),
    UPnP_MediaRenderer1("urn:schemas-upnp-org:device:MediaRenderer:1"),
    UPnP_ContentDirectory1("urn:schemas-upnp-org:service:ContentDirectory:1"),
    UPnP_RenderingControl1("urn:schemas-upnp-org:service:RenderingControl:1"),
    UPnP_ConnectionManager1("urn:schemas-upnp-org:service:ConnectionManager:1"),
    UPnP_AVTransport1("urn:schemas-upnp-org:service:AVTransport:1"),

    // UPnP Microsoft A/V profile
    Microsoft_MediaReceiverRegistrar1("urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1"),

    // UPnP Sonos
    UPnP_SonosZonePlayer1("urn:schemas-upnp-org:device:ZonePlayer:1"),
    UNKnown("UNKnown");
    public String decs;

    SSDPServiceType(String str){
        decs = str;
    }


    public static SSDPServiceType getWithString(String str) {
        switch (str) {
            case "ssdp:all":
                return SSDPServiceType.All;
            case "upnp:rootdevice":
                return SSDPServiceType.UPnP_RootDevice;

            case "urn:schemas-upnp-org:device:InternetGatewayDevice:1":
                return SSDPServiceType.UPnP_InternetGatewayDevice1;
            case "urn:schemas-upnp-org:device:WANConnectionDevice:1":
                return SSDPServiceType.UPnP_WANConnectionDevice1;
            case "urn:schemas-upnp-org:device:WANDevice:1":
                return SSDPServiceType.UPnP_WANDevice1;
            case "urn:schemas-upnp-org:service:WANCommonInterfaceConfig:1":
                return SSDPServiceType.UPnP_WANCommonInterfaceConfig1;
            case "urn:schemas-upnp-org:service:WANIPConnection:1":
                return SSDPServiceType.UPnP_WANIPConnection1;
            case "urn:schemas-upnp-org:service:Layer3Forwarding:1":
                return SSDPServiceType.UPnP_Layer3Forwarding1;

            case "urn:schemas-upnp-org:device:MediaServer:1":
                return SSDPServiceType.UPnP_MediaServer1;
            case "urn:schemas-upnp-org:device:MediaRenderer:1":
                return SSDPServiceType.UPnP_MediaRenderer1;
            case "urn:schemas-upnp-org:service:ContentDirectory:1":
                return SSDPServiceType.UPnP_ContentDirectory1;
            case "urn:schemas-upnp-org:service:RenderingControl:1":
                return SSDPServiceType.UPnP_RenderingControl1;
            case "urn:schemas-upnp-org:service:ConnectionManager:1":
                return SSDPServiceType.UPnP_ConnectionManager1;
            case "urn:schemas-upnp-org:service:AVTransport:1":
                return SSDPServiceType.UPnP_AVTransport1;

            case "urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1":
                return SSDPServiceType.Microsoft_MediaReceiverRegistrar1;

            case "urn:schemas-upnp-org:device:ZonePlayer:1":
                return SSDPServiceType.UPnP_SonosZonePlayer1;

            default:
                return SSDPServiceType.UNKnown;
        }


    }
}
