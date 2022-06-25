package com.example.project3.service;

import com.example.project3.entity.Place;
import com.example.project3.entity.User;
import com.example.project3.repository.PlaceRepository;
import com.example.project3.repository.UserRepository;
import com.example.project3.service.dto.PlaceDTO;
import com.example.project3.service.dto.UserDTO;
import com.example.project3.service.error.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public UserService(UserRepository userRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    public User login(String email, String password){
        Optional<User> userOptional = userRepository.findByEmailAndPassword(email, password);
        if (userOptional.isPresent()){
            User user = new User();
            user = userOptional.get();
            user.setCookie(generatorCookie());
            userRepository.save(user);
            return user;
        }else {
            throw new BadRequestException("error.notFound", null);
        }
    }

    public void logout(long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setCookie(null);
            userRepository.save(user);
        }
    }

    public User loginByCookie(String cookie){
        Optional<User> optionalUser = userRepository.findByCookie(cookie);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        }else {
            return null;
        }
    }

    public List<PlaceDTO> getListPlace(long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            List<Place> list = placeRepository.findAllByUser(optionalUser.get());
            return list.stream().map(PlaceDTO::new).collect(Collectors.toList());
        }else {
            return null;
        }
    }

    public void register(UserDTO userDTO){
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isPresent()){
            throw new BadRequestException("error.existed", null);
        }else {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setName(userDTO.getName());
            userRepository.save(user);
        }
    }

    public void changePassword(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isPresent()){
            User user = new User();
            user = userOptional.get();
            if (user.getPassword().equals(userDTO.getPassword())){
                throw new BadRequestException("error.passwordNotChange", null);
            }else {
                user.setPassword(userDTO.getPassword());
                userRepository.save(user);
            }
        }else {
            throw new BadRequestException("error.notExisted", null);
        }
    }

    public String generatorCookie() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}
