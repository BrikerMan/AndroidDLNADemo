package com.eliyar.bfdlna;

import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import com.eliyar.bfdlna.Exceptions.SendStateListener;

/**
 * Created by brikerman on 2017/5/13.
 */

public class UdpHelper {
    private static final String TAG = "DLNA UdpHelper";

    private int packetLength = 2048;
    private int multicastPort = 1900;
    private InetAddress multicastAddress;
    private Charset charset = Charset.forName("UTF-8");

    private boolean listening;
    private SendStateListener sendListener;

    private DatagramSocket mDatagramSocket;

    public UdpHelper() {
        try {
            this.multicastAddress = InetAddress.getByName("239.255.255.250");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void createSocket() {
        try {
            mDatagramSocket = new DatagramSocket();
            mDatagramSocket.setBroadcast(true);
            Log.v(TAG, "Stared UPD Server @" + mDatagramSocket.getPort());
        } catch (Exception e) {
            Log.e("Error", "初始化失败");
            e.printStackTrace();
        }
    }

    public void startListen(final UdpReceiveListener listener) {
        listening=true;
        createSocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data=new byte[packetLength];
                    while(listening){
                        DatagramPacket datagramPacket=new DatagramPacket(data,data.length);
                        try {
                            mDatagramSocket.receive(datagramPacket);
                            String msg= new String(datagramPacket.getData(),0,datagramPacket.getLength(),charset);

                            Log.i(TAG, "Receive："+msg);
                            new ReceiveThread(msg, datagramPacket.getAddress(), listener).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "初始化失败");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopListen(){
        listening=false;
        if (mDatagramSocket != null) {
            mDatagramSocket.close();
        }
    }

    public void reset() {
        stopListen();
        createSocket();
    }

    public void sendMulticast(String msg){
        send(multicastAddress, multicastPort, msg);
    }

    public void send(final InetAddress address, final int port, final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] messageByte = msg.getBytes();
                    DatagramPacket p = new DatagramPacket(messageByte, messageByte.length);
                    p.setAddress(address);
                    p.setPort(port);
                    mDatagramSocket.send(p);
                    Log.d(TAG, "Send to"+ address.toString() + ":" + port + "\n" + msg );
                    if (sendListener != null) sendListener.success();
                } catch (SocketException e) {
                    e.printStackTrace();
                    if (sendListener != null) sendListener.error();
                }catch( IOException e){
                    e.printStackTrace();
                    if (sendListener != null) sendListener.error();
                }
                Log.d("Send", "sendMulticast");
            }
        }).start();
    }


    public int getPacketLength() {
        return packetLength;
    }
    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }
    public Charset getCharset() {
        return charset;
    }
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    public SendStateListener getSendListener() {
        return sendListener;
    }
    public void setSendListener(SendStateListener sendListener) {
        this.sendListener = sendListener;
    }
}

class ReceiveThread extends Thread{
    private String msg;
    private InetAddress address;
    private UdpReceiveListener listener;


    public ReceiveThread(String msg, InetAddress address,
                         UdpReceiveListener listener) {
        this.msg = msg;
        this.address = address;
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.udpReceive(address, msg);
    }
}