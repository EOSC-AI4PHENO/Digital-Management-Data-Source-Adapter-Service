package com.siseth.adapter.feign.weatherStation;

import com.siseth.adapter.feign.user.UserService;
import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.WeatherStationShortResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherStationServiceImpl {

    private final WeatherStationService service;


    public WeatherStationShortResDTO findById(Long id) {
        try {
            return service.findById(id);
        } catch (Exception e) {
            log.error("Error when try connect to Weather-Station-Service, message {}", e.getMessage());
            return null;
        }
    }

    public WeatherStationShortResDTO saveWeatherStations(String edwinId) {
        try {
            return service.saveWeatherStations(edwinId);
        } catch (Exception e) {
            log.error("Error when try connect to Weather-Station-Service, message {}", e.getMessage());
            return null;
        }
    }


}
