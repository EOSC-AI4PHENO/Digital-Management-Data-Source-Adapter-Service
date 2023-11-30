package com.siseth.adapter.feign.weatherStation.dto.source.request;

import com.siseth.adapter.entity.source.ImageSource;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ImageSourceReqDTO {

    private String name;

    private String desc;

    private ImageSource.SourceType type;

    private ImageSource.SourceRecorded recordedType;

    private Double latitude;

    private Double longitude;

    private String producer;

    private String zoom;

    private String place;

    private OffsetDateTime installationAt;

    private Integer azimuth;

    private Integer inclination;

    private String serialNumber;

    private String edwinId;
}
