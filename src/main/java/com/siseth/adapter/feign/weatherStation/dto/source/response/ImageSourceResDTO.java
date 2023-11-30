package com.siseth.adapter.feign.weatherStation.dto.source.response;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceResDTO extends ImageSourceShortResDTO{


    private ImageSource.SourceType type;

    private String serialNumber;

    private String directory;

    private Boolean isActive;

    private String userId;

    private String producer;

    private String zoom;


    private OffsetDateTime installationAt;

    private Integer azimuth;

    private Integer inclination;

    private Long stationId;

    private String dataEntryPerson;

    private WeatherStationShortResDTO weatherStation;

    private Boolean incremental;


    public ImageSourceResDTO(ImageSource source, String _userId) {
        super(source, _userId);
        type = source.getType();
        serialNumber = source.getSerialNumber();
        directory = source.getDirectory();
        isActive = source.getIsActive();
        userId = source.getUserId();
        producer = source.getProducer();
        zoom = source.getZoom();
        installationAt = source.getInstallationAt();
        azimuth = source.getAzimuth();
        inclination = source.getInclination();
        stationId = source.getStationId();
    }

    public ImageSourceResDTO(ImageSource source) {
        super(source);
        type = source.getType();
        serialNumber = source.getSerialNumber();
        directory = source.getDirectory();
        isActive = source.getIsActive();
        userId = source.getUserId();
        producer = source.getProducer();
        zoom = source.getZoom();
        installationAt = source.getInstallationAt();
        azimuth = source.getAzimuth();
        inclination = source.getInclination();
        stationId = source.getStationId();
    }

    public ImageSourceResDTO(ImageSource source, String _userId, UserInfoDTO userInfoDTO,  WeatherStationShortResDTO weatherStation) {
        this(source, _userId);
        this.dataEntryPerson = Optional.ofNullable(userInfoDTO)
                                                .map(UserInfoDTO::getUserBasic)
                                                .orElse("");
        this.weatherStation = weatherStation;
    }

    public ImageSourceResDTO(ImageSource source, String _userId, UserInfoDTO userInfoDTO,  WeatherStationShortResDTO weatherStation, Boolean incremental) {
        this(source, _userId, userInfoDTO, weatherStation);
        this.incremental = incremental;
    }


}
