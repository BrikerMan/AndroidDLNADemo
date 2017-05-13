package com.eliyar.bfdlna;

import java.net.InetAddress;

/**
 * Created by brikerman on 2017/5/13.
 */

public interface UdpReceiveListener {
    public void receive(InetAddress address, String msg);
}
