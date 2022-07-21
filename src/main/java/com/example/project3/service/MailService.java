package com.example.project3.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleEmail(String to, String mess){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("phamxuanloc2000@gmail.com");
        message.setTo(to);
        message.setSubject("Cảnh báo");
        message.setText(mess);

        this.javaMailSender.send(message);
    }
}
