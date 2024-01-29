package com.twilightsystem.config;

public class config{
    private String autoreconnectstring = "Internal Exception: java.net.SocketException: Connection reset";

    public String getautoreconnectstring() {
        return autoreconnectstring;
    }

    public void setautoreconnectstring(String autoreconnectstring) {
        this.autoreconnectstring = autoreconnectstring;
    }
}