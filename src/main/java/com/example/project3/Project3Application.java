package com.example.project3;

import com.example.project3.mqtt.OutConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
@EnableScheduling
public class Project3Application {

    private final OutConfiguration outConfiguration;

    public Project3Application(OutConfiguration outConfiguration) {
        this.outConfiguration = outConfiguration;
    }

    public static void main(String[] args) {
        SpringApplication.run(Project3Application.class, args);
    }

}