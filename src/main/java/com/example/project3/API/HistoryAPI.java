package com.example.project3.API;

import com.example.project3.entity.History;
import com.example.project3.service.HistoryService;
import com.example.project3.service.dto.HistoryDTO;
import com.example.project3.service.dto.InfoDTO;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HistoryAPI {

    private final HistoryService historyService;

    public HistoryAPI(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping(value = "/api/gethistorydevice")
    public HistoryDTO getInfo(@CookieValue(value = "project3", defaultValue = "unknown") String cookie, @RequestParam long id){
        return historyService.getHistoryForDevice(cookie, id);
    }

    @GetMapping(value = "/api/gettendata")
    public List<HistoryDTO> getTenData(@RequestParam long id){
        return historyService.getTenData(id);
    }
}
