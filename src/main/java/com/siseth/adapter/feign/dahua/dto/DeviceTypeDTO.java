package com.siseth.adapter.feign.dahua.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeviceTypeDTO {

    private DeviceTypeItemDTO info;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public class DeviceTypeItemDTO {

        private String deviceType;

        private String hardwareVersion;

        private String serialNumber;


    }

}
