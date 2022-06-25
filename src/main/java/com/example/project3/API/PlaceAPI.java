package com.example.project3.API;

import com.example.project3.service.PlaceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceAPI {

    private final PlaceService placeService;

    public PlaceAPI(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping(value = "/api/place")
    public String createPlace(@Validated String namePlace, @CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        return placeService.save(namePlace, cookie);
    }

}
