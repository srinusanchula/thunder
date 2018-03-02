package com.citrix.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userName", "password", "deviceId"})
public class Input {
    private String userName;
    private String password;
    private int deviceId;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Input{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }

    public int getDeviceId() {
        return deviceId;
    }

}
