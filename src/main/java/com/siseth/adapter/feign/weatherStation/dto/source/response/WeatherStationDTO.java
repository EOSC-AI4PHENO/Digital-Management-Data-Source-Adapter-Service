package com.siseth.adapter.feign.weatherStation.dto.source.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WeatherStationDTO {
    private Long stationId;
    private String observationStationId;
    private String name;
    private String tercCode;
    private String wktLocation;
    private Double latitude;
    private Double longitude;
    private String ownerId;
    private StationType stationType;
    private Boolean active;
    private List<LinksDTO> links;

    public String[] getDataRow() {
        return new String[]{
                String.valueOf(this.stationId),
                this.observationStationId,
                this.name,
                this.tercCode,
                this.wktLocation,
                String.valueOf(this.latitude),
                String.valueOf(this.longitude),
                String.valueOf(this.ownerId),
                String.valueOf(this.stationType),
                String.valueOf(this.active)
        };
    }

    public enum StationType {
        WEATHER,
        RAIN,
        UNKNOWN
    }
}