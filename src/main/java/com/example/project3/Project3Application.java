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

//    @Scheduled(fixedRate = 60000)
    public void test() throws IOException {
        try {
            URL url = new URL("https://www.techone.vn/dien-thoai-samsung-a03s.html");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int statusCode = connection.getResponseCode();
            String html = "";
            if (statusCode > 299){
                html = IOUtils.toString(connection.getErrorStream(),"UTF-8");
            }else {
                html = IOUtils.toString(connection.getInputStream(), "UTF-8");
            }
            System.out.println(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}




