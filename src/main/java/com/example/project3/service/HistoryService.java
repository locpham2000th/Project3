package com.example.project3.service;

import com.example.project3.entity.Device;
import com.example.project3.entity.History;
import com.example.project3.entity.Place;
import com.example.project3.entity.User;
import com.example.project3.repository.*;
import com.example.project3.service.dto.AlarmDTO;
import com.example.project3.service.dto.HistoryDTO;
import com.example.project3.service.error.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PlaceRepository placeRepository;
    private final KindDeviceRepository kindDeviceRepository;

    public HistoryService(HistoryRepository historyRepository,
                          UserRepository userRepository,
                          DeviceRepository deviceRepository,
                          PlaceRepository placeRepository,
                          KindDeviceRepository kindDeviceRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.placeRepository = placeRepository;
        this.kindDeviceRepository = kindDeviceRepository;
    }

    public HistoryDTO getHistoryForDevice(String cookie, long id){
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Device> optionalDevice = deviceRepository.findById(id);
            if (optionalDevice.isPresent()){
                Optional<Place> placeOptional = placeRepository.findById(optionalDevice.get().getPlace().getId());
                if (placeOptional.isPresent()){
                    if (placeOptional.get().getUser().getId().equals(user.getId())){
                        Optional<History> historyOptional = historyRepository.findFirstByDeviceOrderByIdDesc(optionalDevice.get());
                        if (historyOptional.isPresent()){
                            return new HistoryDTO(historyOptional.get());
                        }
                    }else {
                        throw new BadRequestException("error.deviceNotBelongToUser", null);
                    }
                }else {
                    throw new BadRequestException("error.placeNotExist", null);
                }
            }else {
                throw new BadRequestException("error.deviceNotExist", null);
            }
        }else {
            throw new BadRequestException("error.cookieExpired", null);
        }
        return null;
    }

    public List<HistoryDTO> getTenData(long id) {
        List<History> historyList = historyRepository.findHistoryByDeviceAndTimeBefore(id, Instant.now(), PageRequest.of(0, 10, Sort.Direction.DESC, "id"));
//        System.out.println(historyList);
        return historyList.stream().map(HistoryDTO::new).collect(Collectors.toList());
    }

    public HistoryDTO getFirstHistory(long id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isPresent()){
            Optional<History> historyOptional = historyRepository.findFirstByDeviceOrderByIdDesc(deviceOptional.get());
            if (historyOptional.isPresent()){
                return new HistoryDTO(historyOptional.get());
            }
        }
        return null;
    }
}
