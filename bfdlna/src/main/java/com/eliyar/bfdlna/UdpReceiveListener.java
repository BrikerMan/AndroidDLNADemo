package com.eliyar.bfdlna;

import java.net.InetAddress;

/**
 * Created by brikerman on 2017/5/13.
 */

public interface UdpReceiveListener {
    /**
     * udp 服务收到消息
     * @param address   发送消息 ip
     * @param msg       接收到的消息
     */
    public void udpReceive(InetAddress address, String msg);
}
