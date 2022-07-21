package com.example.project3.service;

import com.example.project3.entity.*;
import com.example.project3.mqtt.OutConfiguration;
import com.example.project3.repository.*;
import com.example.project3.service.dto.DataDraw;
import com.example.project3.service.dto.DeviceDTO;
import com.example.project3.service.dto.InfoDTO;
import com.example.project3.service.error.BadRequestException;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final OutConfiguration outConfiguration;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PlaceRepository placeRepository;
    private final HistoryRepository historyRepository;
    private final KindDeviceRepository kindDeviceRepository;

    public DeviceService(OutConfiguration outConfiguration, UserRepository userRepository, DeviceRepository deviceRepository, PlaceRepository placeRepository, HistoryRepository historyRepository, KindDeviceRepository kindDeviceRepository) {
        this.outConfiguration = outConfiguration;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.placeRepository = placeRepository;
        this.historyRepository = historyRepository;
        this.kindDeviceRepository = kindDeviceRepository;
    }

    public void commandToDevice(int idDevice, boolean active){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", idDevice);
        jsonObject.put("active", active);
        MessageChannel messageChannel = outConfiguration.out();
        MqttPahoMessageHandler mqttPahoMessageHandler = outConfiguration.outboundAdapter("locpxcommand", null);
        Message<String> message = MessageBuilder.withPayload(jsonObject.toJSONString()).build();
        messageChannel.send(message);
        outConfiguration.outboundFlow(messageChannel, mqttPahoMessageHandler);
    }

    @Bean
    MqttPahoClientFactory clientFactory(@Value("${hivemq.uri}") String host){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{host});
        factory.setConnectionOptions(options);
        return factory;
    }

    public List<InfoDTO> getInfo(String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        List<InfoDTO> infoDTOS = new ArrayList<>();
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            List<Place> places = placeRepository.findAllByUser(user);
            for (Place place: places){
                List<String> names = new ArrayList<>();
                List<String> values = new ArrayList<>();
                List<Long> ids = new ArrayList<>();
                List<Boolean> realTimes = new ArrayList<>();
                List<Boolean> actives = new ArrayList<>();
                List<Device> deviceList = deviceRepository.findAllByPlace(place);
                for (Device device : deviceList){
                    Optional<History> historyOptional = historyRepository.findFirstByDeviceOrderByIdDesc(device);
                    if (historyOptional.isPresent()){
                        History history = historyOptional.get();
                        Optional<KindDevice> kindDevice = kindDeviceRepository.findById(device.getKindDevice().getId());
                        System.out.println(history.getValue());
                        names.add(device.getName());
                        values.add(history.getValue());
                        ids.add(device.getId());
                        realTimes.add(kindDevice.get().isRealTime());
                        actives.add(device.isActive());
                    }
                }
                infoDTOS.add(new InfoDTO(user, place,ids, names, values, realTimes, actives));
            }
        }
        return infoDTOS;
    }

    public List<DataDraw> getDataForDraw(String cookie) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            List<DataDraw> dataDrawList = new ArrayList<>();
            List<Place> placeList = placeRepository.findAllByUser(userOptional.get());
            for (int i = 0; i < placeList.size(); i++){
                List<Device> deviceList = deviceRepository.findAllByPlace(placeList.get(i));
                for (int j = 0; j < deviceList.size(); j++){
                    Optional<KindDevice> kindDevice = kindDeviceRepository.findById(deviceList.get(j).getKindDevice().getId());
                    Optional<History> historyOptional = historyRepository.findFirstByDeviceOrderByIdDesc(deviceList.get(j));
                    if (historyOptional.isPresent()){
                        DataDraw dataDraw = new DataDraw();
                        dataDraw.setIdDevice(deviceList.get(j).getId());
                        dataDraw.setNameDevice(deviceList.get(j).getName());
                        dataDraw.setActive(deviceList.get(j).isActive());
                        dataDraw.setRealTime(kindDevice.get().isRealTime());
                        dataDraw.setHistory(historyOptional.get());
                        dataDrawList.add(dataDraw);
                    }
                }
            }
            return dataDrawList;
        }else {
            throw new BadRequestException("error.cookieExpaid", null);
        }
    }

    public void createDevice(DeviceDTO deviceDTO) {
        Optional<Place> placeOptional = placeRepository.findById(deviceDTO.getIdPlace());
        if (placeOptional.isPresent()){
            Optional<KindDevice> kindDeviceOptional = kindDeviceRepository.findById(deviceDTO.getIdKindDevice());
            if (kindDeviceOptional.isPresent()){
                Optional<Device> deviceOptional = deviceRepository.findByPlaceAndName(placeOptional.get(), deviceDTO.getName());
                if (!deviceOptional.isPresent()){
                    Device device = new Device();
                    device.setKindDevice(kindDeviceOptional.get());
                    device.setActive(true);
                    device.setPlace(placeOptional.get());
                    device.setName(deviceDTO.getName());
                    deviceRepository.save(device);
                }else {
                    throw new BadRequestException("error.deviceExist", null);
                }
            }
        }
    }

    public List<DeviceDTO> getDeviceOfPlace(long id) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()){
            List<Device> deviceList = deviceRepository.findAllByPlace(placeOptional.get());
            return deviceList.stream().map(DeviceDTO::new).collect(Collectors.toList());
        }else {
            throw new BadRequestException("error.placeNotExist", null);
        }
    }

    public boolean changeDeviceStatus(long id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            device.setActive(!device.isActive());
            deviceRepository.save(device);
            return device.isActive();
        }else {
            throw new BadRequestException("error.deviceNotExist", null);
        }
    }

    public void deleteDevice(long id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            deviceRepository.delete(device);
        }else {
            throw new BadRequestException("error.deviceNotExist", null);
        }
    }

    public List<DataDraw> getDataForDrawInPlace(String cookie, long id) {
        if (cookie.equals("unknown")){
            throw new BadRequestException("error.notCookie", null);
        }
        Optional<User> userOptional = userRepository.findByCookie(cookie);
        if (userOptional.isPresent()){
            List<DataDraw> dataDrawList = new ArrayList<>();
            List<Place> placeList = placeRepository.findAllByUserAndId(userOptional.get(), id);
            for (int i = 0; i < placeList.size(); i++){
                List<Device> deviceList = deviceRepository.findAllByPlace(placeList.get(i));
                for (int j = 0; j < deviceList.size(); j++){
                    Optional<KindDevice> kindDevice = kindDeviceRepository.findById(deviceList.get(j).getKindDevice().getId());
                    Optional<History> historyOptional = historyRepository.findFirstByDeviceOrderByIdDesc(deviceList.get(j));
                    if (historyOptional.isPresent()){
                        DataDraw dataDraw = new DataDraw();
                        dataDraw.setIdDevice(deviceList.get(j).getId());
                        dataDraw.setNameDevice(deviceList.get(j).getName());
                        dataDraw.setActive(deviceList.get(j).isActive());
                        dataDraw.setRealTime(kindDevice.get().isRealTime());
                        dataDraw.setHistory(historyOptional.get());
                        dataDrawList.add(dataDraw);
                    }
                }
            }
            return dataDrawList;
        }else {
            throw new BadRequestException("error.cookieExpaid", null);
        }
    }
}
