package com.siseth.adapter.feign.dahua.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeviceInfoItemDTO {

    private String name;

    private String state;

    private List<DeviceInfoItemDetailDTO> details;

}
