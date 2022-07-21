package com.example.project3.API;

import com.example.project3.entity.Place;
import com.example.project3.service.PlaceService;
import com.example.project3.service.dto.PlaceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaceAPI {

    private final PlaceService placeService;

    public PlaceAPI(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping(value = "/api/place")
    public ResponseEntity<?> createPlace(@Validated PlaceDTO placeDTO, @CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        return new ResponseEntity<>(placeService.save(placeDTO, cookie), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/api/place/{id}")
    public void DeletePlace(@PathVariable(name = "id") long id, @CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        placeService.deletePlace(id, cookie);
    }

    @GetMapping(value = "/api/place")
    public ResponseEntity<?> getAll(@CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        List<PlaceDTO> placeDTOList = placeService.getAll(cookie);
        return new ResponseEntity<>(placeDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/api/place/{id}")
    public ResponseEntity<?> getInfoPlace(@PathVariable long id, @CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        PlaceDTO placeDTO = placeService.getInfoPlace(id, cookie);
        return new ResponseEntity<>(placeDTO, HttpStatus.OK);
    }

}
