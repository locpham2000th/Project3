package com.example.project3.service.dto;

import com.example.project3.entity.History;

import java.time.Instant;

public class HistoryDTO {

    private Long id;

    private long deviceId;

    private String value;

    private Instant time;

    public HistoryDTO(History history) {
        this.id = history.getId();
        this.deviceId = history.getDevice().getId();
        this.value = history.getValue();
        this.time = history.getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
