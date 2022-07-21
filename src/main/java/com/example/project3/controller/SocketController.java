package com.example.project3.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SocketController {

    @MessageMapping("/hello")
    @SendTo("/loc/topic/greetings")
    public String show(){
        System.out.println("topic");
        List<String> list = new ArrayList<>();
        list.add("loc");
        return "loc";
    }

}
