package com.titanicos.TitanicInventory.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("testLog")
public class TestLogEvent {
    public TestLogEvent(String action, String user, String ip) {
        this.action = action;
        this.user = user;
        this.ip = ip;
        this.timestamp = new Date();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    private String action;
    private String user;
    private String ip;
    private Date timestamp;

}
