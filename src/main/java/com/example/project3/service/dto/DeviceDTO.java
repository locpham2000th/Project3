package com.example.project3.service.dto;

import com.example.project3.entity.Device;
import com.example.project3.entity.KindDevice;
import com.example.project3.entity.Place;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

public class DeviceDTO {

    public DeviceDTO() {
    }

    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.name = device.getName();
        this.idPlace = device.getPlace().getId();
        this.idKindDevice = device.getKindDevice().getId();
        this.active = device.isActive();
        this.realTime = device.getKindDevice().isRealTime();
    }

    private Long id;

    private String name;

    private long idPlace;

    private long idKindDevice;

    private boolean active;

    private boolean realTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    public long getIdKindDevice() {
        return idKindDevice;
    }

    public void setIdKindDevice(long idKindDevice) {
        this.idKindDevice = idKindDevice;
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
}
