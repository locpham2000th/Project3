package com.example.project3.service;

import com.example.project3.entity.*;
import com.example.project3.repository.*;
import com.example.project3.service.dto.AlarmDTO;
import org.json.simple.*;
import org.json.simple.JSONValue;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public ReceiveData(DeviceRepository deviceRepository, KindDeviceRepository kindDeviceRepository, HistoryRepository historyRepository, PlaceRepository placeRepository, UserRepository userRepository, MailService mailService, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.deviceRepository = deviceRepository;
        this.kindDeviceRepository = kindDeviceRepository;
        this.historyRepository = historyRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
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

                    Optional<Place> placeOptional = placeRepository.findById(device.getPlace().getId());
                    AlarmDTO alarmDTO = new AlarmDTO();
                    Optional<User> userOptional = userRepository.findById(placeOptional.get().getUser().getId());
                    if (value > optionalKindDevice.get().getMax()){
                        String titleMax = optionalKindDevice.get().getTitleMax();
                        alarmDTO.setNamePlace(placeOptional.get().getName());
                        alarmDTO.setNameDevice(device.getName());
                        alarmDTO.setTitle(titleMax.replace("{}", history.getValue()) + optionalKindDevice.get().getMax() + optionalKindDevice.get().getUnit());
                        sendAlarm( userOptional.get().getId(), alarmDTO);
                    }else if (value < optionalKindDevice.get().getMin()){
                        String titleMin = optionalKindDevice.get().getTitleMin();
                        alarmDTO.setNamePlace(placeOptional.get().getName());
                        alarmDTO.setNameDevice(device.getName());
                        alarmDTO.setTitle(titleMin.replace("{}", history.getValue()) + optionalKindDevice.get().getMin() + optionalKindDevice.get().getUnit());
                        sendAlarm( userOptional.get().getId(), alarmDTO);
                    }
                }
            }
        }
    }


    public void sendAlarm(long id, AlarmDTO alarmDTO){
//        String idUser = ;
        simpMessageSendingOperations.convertAndSendToUser(String.valueOf(id),"/topic/greetings", alarmDTO);

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            mailService.sendSimpleEmail(user.getEmail(), alarmDTO.getTitle());
        }

    }


}
