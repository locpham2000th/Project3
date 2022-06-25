package com.example.project3.service.dto;

import com.example.project3.entity.History;

public class DataDraw {

    private long idDevice;

    private String nameDevice;

    private boolean active;

    private boolean realTime;

    private History history;

    public long getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(long idDevice) {
        this.idDevice = idDevice;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public void setRealTime(boolean realTime) {
        this.realTime = realTime;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
