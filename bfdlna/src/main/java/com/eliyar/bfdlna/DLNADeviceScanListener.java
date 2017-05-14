package com.eliyar.bfdlna;

import com.eliyar.bfdlna.SSDP.SSDPDevice;

/**
 * Created by brikerman on 2017/5/13.
 */

public interface DLNADeviceScanListener {
    public void didFoundDevice(SSDPDevice SSDPDevice);
}
