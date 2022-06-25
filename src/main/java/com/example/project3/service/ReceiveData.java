package com.example.project3.service;

import com.example.project3.entity.Device;
import com.example.project3.entity.History;
import com.example.project3.entity.KindDevice;
import com.example.project3.repository.DeviceRepository;
import com.example.project3.repository.HistoryRepository;
import com.example.project3.repository.KindDeviceRepository;
import org.json.simple.*;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiveData {

    private final DeviceRepository deviceRepository;
    private final KindDeviceRepository kindDeviceRepository;
    private final HistoryRepository historyRepository;

    public ReceiveData(DeviceRepository deviceRepository, KindDeviceRepository kindDeviceRepository, HistoryRepository historyRepository) {
        this.deviceRepository = deviceRepository;
        this.kindDeviceRepository = kindDeviceRepository;
        this.historyRepository = historyRepository;
    }

    public void receiveData(String data){
        Object object = JSONValue.parse(data);
        JSONArray jsonArray = (JSONArray) JSONValue.parse(data);
        Iterator iterator = jsonArray.iterator();
        List<Device> deviceList = new ArrayList<>();
//        deviceList = deviceRepository.findAllById()
        while (iterator.hasNext()){
            JSONObject object1  = (JSONObject) iterator.next();
            long id = (long) object1.get("deviceId");
            long value = (long) object1.get("data");
            long second = (long) object1.get("time");
            Optional<Device> optionalDevice = deviceRepository.findById(id);
            if (optionalDevice.isPresent()){
                Device device = optionalDevice.get();
                Optional<KindDevice> optionalKindDevice = kindDeviceRepository.findById(device.getKindDevice().getId());
                if (optionalKindDevice.isPresent()){
                History history = new History();
                history.setDevice(device);
                history.setValue(String.valueOf(value + optionalKindDevice.get().getUnit()));
                System.out.println(second + " -> " + Instant.ofEpochMilli(second));
                history.setTime(Instant.ofEpochMilli(second));
                historyRepository.save(history);
                }
            }
//            long second = (long) object1.get("time");
//            Instant instant = Instant.ofEpochMilli(second);
//            System.out.println(object1.get("data"));
//            System.out.println(instant);


        }
    }

}
