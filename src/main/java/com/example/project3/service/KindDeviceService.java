package com.example.project3.service;

import com.example.project3.entity.KindDevice;
import com.example.project3.repository.KindDeviceRepository;
import com.example.project3.service.dto.KindDeviceDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KindDeviceService {

    private final KindDeviceRepository kindDeviceRepository;

    public KindDeviceService(KindDeviceRepository kindDeviceRepository) {
        this.kindDeviceRepository = kindDeviceRepository;
    }


    public List<KindDeviceDTO> getAll() {
        List<KindDevice> kindDevices = kindDeviceRepository.findAll();
        return kindDevices.stream().map(KindDeviceDTO::new).collect(Collectors.toList());
    }
}
