package com.example.project3.service.dto;

import com.example.project3.entity.Place;
import com.example.project3.entity.User;

public class PlaceDTO {

    public PlaceDTO() {
    }

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.user = place.getUser();
        this.lat = place.getLat();
        this.lng = place.getLng();
    }

    private Long id;

    private String name;

    private double lat;

    private double lng;

    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
