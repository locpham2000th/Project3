package com.example.project3.service.dto;


import com.example.project3.entity.KindDevice;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class KindDeviceDTO {

    public KindDeviceDTO(KindDevice kindDevice) {
        this.id = kindDevice.getId();
        this.name = kindDevice.getName();
        this.value = kindDevice.getValue();
        this.random = kindDevice.getRandom();
        this.unit = kindDevice.getUnit();
        this.realTime = kindDevice.isRealTime();
        this.min = kindDevice.getMin();
        this.max = kindDevice.getMax();
        this.titleMax = kindDevice.getTitleMax();
        this.titleMin = kindDevice.getTitleMin();
    }

    private Long id;

    private String name;

    private String value;

    private String random;

    private String unit;

    private boolean realTime;

    private int max;

    private int min;

    private String titleMin;

    private String titleMax;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public void setRealTime(boolean realTime) {
        this.realTime = realTime;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getTitleMin() {
        return titleMin;
    }

    public void setTitleMin(String titleMin) {
        this.titleMin = titleMin;
    }

    public String getTitleMax() {
        return titleMax;
    }

    public void setTitleMax(String titleMax) {
        this.titleMax = titleMax;
    }
}
