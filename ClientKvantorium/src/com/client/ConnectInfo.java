package com.client;

public class ConnectInfo {
    private String ip;
    private int port;

    public ConnectInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}