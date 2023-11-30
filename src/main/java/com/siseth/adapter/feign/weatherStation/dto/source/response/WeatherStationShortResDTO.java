package com.siseth.adapter.feign.weatherStation.dto.source.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class WeatherStationShortResDTO {

    private Long stationId;
    private String observationStationId;
    private String name;
    private Double latitude;
    private Double longitude;
    private WeatherStationDTO.StationType stationType;


}
