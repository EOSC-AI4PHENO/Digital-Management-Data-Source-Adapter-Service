package com.siseth.adapter.feign.weatherStation.dto.source.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeteoDataResponseDTO {
    private List<MeteoDataDTO> meteoDataList;
    private List<LinksDTO> links;
    private PageDTO page;
}