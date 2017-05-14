package com.eliyar.bfdlna;

import com.eliyar.bfdlna.SSDP.Device;

import java.util.ArrayList;

/**
 * Created by brikerman on 2017/5/13.
 */

public interface DLNADeviceScanListener {
    public void didFoundDevice(Device device);
}
