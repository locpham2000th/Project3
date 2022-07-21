package com.example.project3.API;

import com.example.project3.entity.Device;
import com.example.project3.service.DeviceService;
import com.example.project3.service.dto.DataDraw;
import com.example.project3.service.dto.DeviceDTO;
import com.example.project3.service.dto.InfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/api/getdatafordrawinplace/{id}")
    public List<DataDraw> getDataForDrawInPlace(@CookieValue(value = "project3", defaultValue = "unknown") String cookie, @PathVariable(value = "id") long id){
        return deviceService.getDataForDrawInPlace(cookie, id);
    }

    @PostMapping(value = "/api/device")
    public void createDevice(@Validated DeviceDTO deviceDTO){
        deviceService.createDevice(deviceDTO);
    }

    @GetMapping(value = "/api/device")
    public ResponseEntity<?> getDevice(@RequestParam(name = "idPlace") long id){
        List<DeviceDTO> deviceDTOS = deviceService.getDeviceOfPlace(id);
        return new ResponseEntity<>(deviceDTOS, HttpStatus.OK);
    }

    @PutMapping(value = "/api/device/change/{id}")
    public boolean changeStatusDevice(@PathVariable(value = "id") long id){
        boolean result = deviceService.changeDeviceStatus(id);
        return result;
    }

    @DeleteMapping(value = "/api/device/delete/{id}")
    public void deleteDevice(@PathVariable("id") long id){
        deviceService.deleteDevice(id);
    }

}
