package com.siseth.adapter.feign.weatherStation.dto.source.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceConfigReqDTO {

    private Long id;

    private String url;

    private Integer port;

    private String cameraUser;

    private String cameraPassword;

    private Boolean enable;

    public Boolean getEnable() {
        return Optional.ofNullable(this.enable).orElse(false);
    }

}
