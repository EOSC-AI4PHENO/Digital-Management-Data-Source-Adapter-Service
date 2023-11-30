package com.siseth.adapter.feign.weatherStation.dto.source.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class MeteoDataDTO {
    private Long stationId;
    private String measurementDate;
    private Double airTemperature;
    private Double relativeHumidity;
    private Double insolation;
    private Double windSpeed;
    private Double windDirection;
    private Double airPressure;
    private Double precipitation;
    private Double dewPointTemperature;
    private List<LinksDTO> links;

    public String[] getDataRow() {
        return new String[]{
                String.valueOf(this.stationId),
                this.measurementDate,
                String.valueOf(this.airTemperature),
                String.valueOf(this.relativeHumidity),
                String.valueOf(this.insolation),
                String.valueOf(this.windSpeed),
                String.valueOf(this.windDirection),
                String.valueOf(this.airPressure),
                String.valueOf(this.precipitation),
                String.valueOf(this.dewPointTemperature)
        };
    }
}