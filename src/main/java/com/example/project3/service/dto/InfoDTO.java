package com.example.project3.service.dto;

import com.example.project3.entity.Device;
import com.example.project3.entity.History;
import com.example.project3.entity.Place;
import com.example.project3.entity.User;

import java.util.List;

public class InfoDTO {

    private long idUser;
    private long idPlace;
    private String namePlace;
    private List<Long> ids;
    private List<String> nameDevice;
    private List<String> value;
    private List<Boolean> realTimes;
    private List<Boolean> actives;

    public InfoDTO(User user, Place place,List<Long> ids, List<String> names, List<String> values, List<Boolean> realTimes, List<Boolean> actives) {
        this.idUser = user.getId();
        this.idPlace = place.getId();
        this.namePlace = place.getName();
        this.ids = ids;
        this.nameDevice = names;
        this.value = values;
        this.realTimes = realTimes;
        this.actives = actives;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<String> getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(List<String> nameDevice) {
        this.nameDevice = nameDevice;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public List<Boolean> getRealTimes() {
        return realTimes;
    }

    public void setRealTimes(List<Boolean> realTimes) {
        this.realTimes = realTimes;
    }

    public List<Boolean> getActive() {
        return actives;
    }

    public void setActive(List<Boolean> active) {
        this.actives = active;
    }
}
