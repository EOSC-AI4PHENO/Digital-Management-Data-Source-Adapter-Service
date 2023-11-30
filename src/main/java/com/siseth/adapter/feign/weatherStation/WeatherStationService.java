package com.siseth.adapter.feign.weatherStation;

import com.siseth.adapter.feign.weatherStation.dto.source.response.MeteoDataResponseDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.WeatherStationDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.WeatherStationShortResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@FeignClient(name = "weather-station-service")
public interface WeatherStationService {

    @GetMapping("/api/weather-station/station/meteoData/forceDownload/byStationId")
    public MeteoDataResponseDTO getMeteoDataForStationInTime(@RequestParam(required = false, name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                       @RequestParam(required = false, name = "endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                       @RequestParam(name = "stationId") Long stationId,
                                                       @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                       @RequestParam(name = "size", defaultValue = "10000") int pageSize,
                                                       @RequestParam(required = false, defaultValue = "asc") String sort);

    @GetMapping("/api/weather-station/station/weatherStation/byId")
    public WeatherStationDTO getWeatherStations(@RequestParam(name = "stationId") Long stationId);

    @GetMapping("/api/internal/weather-station/station/weatherStation/id")
    WeatherStationShortResDTO findById(@RequestParam Long id);

    @PutMapping("/api/internal/weather-station/station/weatherStation/edwin")
    WeatherStationShortResDTO saveWeatherStations(@RequestParam String edwinId);


}
