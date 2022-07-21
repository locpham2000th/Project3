package com.example.project3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Controler {

//    @RequestMapping("/")
//    public ModelAndView index(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
//    }
//
//    @RequestMapping("/home")
//    public ModelAndView home(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("home");
////        System.out.println(id);
//        return modelAndView;
//    }

    @RequestMapping("/register")
    public ModelAndView register(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping("/place/{id}")
    public ModelAndView addPlace(@PathVariable long id ){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("place");
        return modelAndView;
    }

}
