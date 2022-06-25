package com.example.project3.API;

import com.example.project3.entity.User;
import com.example.project3.service.UserService;
import com.example.project3.service.dto.PlaceDTO;
import com.example.project3.service.dto.UserDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class UserAPI {

    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserDTO> login(@Validated String email, String password){
        User user = userService.login(email, password);
        ResponseCookie cookie = ResponseCookie.from("project3", user.getCookie()).maxAge(60 * 60).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .body(new UserDTO(user));
//                .build();
//        return new UserDTO(user);
    }

    @GetMapping(value = "/")
    public ModelAndView index(@CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(cookie);
        User user = userService.loginByCookie(cookie);
        if (user != null){
            modelAndView.addObject("user", user);
            modelAndView.setViewName("home");
        }else {
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @GetMapping(value = "/logout")
    public void logout(@RequestParam long id){
        userService.logout(id);
    }

    @GetMapping(value = "/place/getlist/{iduser}")
    public List<PlaceDTO> getInfo(@PathVariable(value = "iduser") long id){
        List<PlaceDTO> result = userService.getListPlace(id);
        return result;
    }

    @PostMapping(value = "api/register")
    public void register(UserDTO userDTO){
        userService.register(userDTO);
    }

    @PutMapping(value = "/changepassword")
    public void changePassword(@RequestBody UserDTO userDTO){
        userService.changePassword(userDTO);
    }

}
