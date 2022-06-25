package com.example.project3.service;

import com.example.project3.entity.Place;
import com.example.project3.entity.User;
import com.example.project3.repository.PlaceRepository;
import com.example.project3.repository.UserRepository;
import com.example.project3.service.error.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    public String save(String namePlace, @NotNull String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
//        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()){
            Optional<Place> placeOptional = placeRepository.findByUserAndName(userOptional.get(), namePlace);
            if (placeOptional.isPresent()){
                throw new BadRequestException("error.placeExisted", null);
            }else {
                Place place = new Place();
                place.setName(namePlace);
                place.setUser(userOptional.get());
                placeRepository.save(place);
                return "success";
            }
        }else {
            throw new BadRequestException("error.userNotExisted", null);
        }

    }
}
