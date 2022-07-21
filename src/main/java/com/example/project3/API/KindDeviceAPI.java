package com.example.project3.API;

import com.example.project3.entity.KindDevice;
import com.example.project3.service.KindDeviceService;
import com.example.project3.service.dto.KindDeviceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KindDeviceAPI {

    private final KindDeviceService kindDeviceService;

    public KindDeviceAPI(KindDeviceService kindDeviceService) {
        this.kindDeviceService = kindDeviceService;
    }

    @GetMapping(value = "/api/KindDevice")
    public ResponseEntity<?> getAll(){
        List<KindDeviceDTO> kindDeviceDTOList = kindDeviceService.getAll();
        return new ResponseEntity<>(kindDeviceDTOList, HttpStatus.OK);
    }

}
