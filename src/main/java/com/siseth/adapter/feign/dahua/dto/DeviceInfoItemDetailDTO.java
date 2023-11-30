package com.siseth.adapter.feign.dahua.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeviceInfoItemDetailDTO {

    private String path;

    private String totalBytes;

    private String type;

    private String usedBytes;

}
