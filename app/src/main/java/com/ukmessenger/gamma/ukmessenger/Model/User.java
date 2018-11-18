package com.ukmessenger.gamma.ukmessenger.Model;

public class User {
    private String uid;
    private String carnet;
    private String name;
    private String status;

    public User(String uid, String carnet, String name, String status) {
        this.uid = uid;
        this.carnet = carnet;
        this.name = name;
        this.status = status;
    }

    public User() { }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
