package com.example.project3.service;

import com.example.project3.entity.Place;
import com.example.project3.entity.User;
import com.example.project3.repository.PlaceRepository;
import com.example.project3.repository.UserRepository;
import com.example.project3.service.dto.PlaceDTO;
import com.example.project3.service.error.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    public String save(PlaceDTO placeDTO, @NotNull String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
//        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()){
            Optional<Place> placeOptional = placeRepository.findByUserAndName(userOptional.get(), placeDTO.getName());
            if (placeOptional.isPresent()){
                throw new BadRequestException("error.placeExisted", null);
            }else {
                Place place = new Place();
                place.setName(placeDTO.getName());
                place.setUser(userOptional.get());
                place.setLat(placeDTO.getLat());
                place.setLng(placeDTO.getLng());
                placeRepository.save(place);
                return "success";
            }
        }else {
            throw new BadRequestException("error.userNotExisted", null);
        }

    }

    public void deletePlace(long id, String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            Optional<Place> placeOptional = placeRepository.findById(id);
            if (placeOptional.isPresent()){
                Place place = placeOptional.get();
                if(userOptional.get().getId().equals(place.getUser().getId())){
                    placeRepository.delete(place);
                }else {
                    throw new BadRequestException("error.noPermissionToDelete", null);
                }
            }else {
                throw new BadRequestException("error.placeNotExist", null);
            }
        }
    }

    public List<PlaceDTO> getAll(String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            List<PlaceDTO> placeDTOList = placeRepository.findAllByUser(userOptional.get()).stream().map(PlaceDTO::new).collect(Collectors.toList());
            return placeDTOList;
        }else {
            throw new BadRequestException("error.userNotExist", null);
        }
    }

    public PlaceDTO getInfoPlace(long id, String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            Optional<Place> placeOptional = placeRepository.findByUserAndId(userOptional.get(), id);
            if (placeOptional.isPresent()){
                return new PlaceDTO(placeOptional.get());
            }else {
                throw new BadRequestException("error.placeNotExist", null);
            }
        }else {
            throw new BadRequestException("error.userNotExist", null);
        }
    }
}
