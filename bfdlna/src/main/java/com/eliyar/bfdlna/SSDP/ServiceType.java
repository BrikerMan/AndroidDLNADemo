package com.eliyar.bfdlna.SSDP;

/**
 * Created by brikerman on 2017/5/13.
 */

public enum ServiceType {
    All("ssdp:all"),
    UPnP_RootDevice("upnp:rootdevice"),

    // UPnP Internet Gateway Device (IGD)
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
    UPnP_AVTransport1 ("urn:schemas-upnp-org:service:AVTransport:1"),

    // UPnP Microsoft A/V profile
    Microsoft_MediaReceiverRegistrar1("urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1"),

    // UPnP Sonos
    UPnP_SonosZonePlayer1("urn:schemas-upnp-org:device:ZonePlayer:1"),
    UNKnown("UNKnown");
    public String decs;

    ServiceType(String str){
        decs = str;
    }
}
