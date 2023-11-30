package com.siseth.adapter.feign.weatherStation.dto;

import com.siseth.adapter.feign.weatherStation.dto.source.response.LinksDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.MeteoDataDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.PageDTO;
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