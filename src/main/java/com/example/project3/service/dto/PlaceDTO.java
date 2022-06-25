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
    }

    private Long id;

    private String name;

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

}
