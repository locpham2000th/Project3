package com.example.project3.API;

import com.example.project3.service.DeviceService;
import com.example.project3.service.dto.DataDraw;
import com.example.project3.service.dto.InfoDTO;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeviceAPI {

    private final DeviceService deviceService;

    public DeviceAPI(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping(value = "/api/getinfo")
    public List<InfoDTO> getInfo(@CookieValue(value = "project3", defaultValue = "unknown") String cookie){
        return deviceService.getInfo(cookie);
    }

    @GetMapping(value = "/api/getdatafordraw")
    public List<DataDraw> getDataForDraw(@CookieValue(value = "project3", defaultValue = "unknown") String cookie){
       return deviceService.getDataForDraw(cookie);
    }

}
