package com.siseth.adapter.feign.weatherStation.dto.source.response;

import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceShortResDTO {

    private Long id;
    private Double longitude;
    private Double latitude;
    private String name;
    private String userId;

    private String desc;

    private ImageSource.SourceRecorded recorded;

    private OffsetDateTime synchronizedAt;

    private OffsetDateTime lastPhotoUploadAt;

    private String url;

    private Integer port;

    private String place;

    private String cameraUser;

    private String cameraPassword;

    private OffsetDateTime createdAt;

    private Boolean isOwner;


    public ImageSourceShortResDTO(ImageSource source) {
        this.id = source.getId();
        this.longitude = source.getLongitude();
        this.latitude = source.getLatitude();
        this.name = source.getName();
        this.userId = source.getUserId();
        this.desc = source.getDesc();
        this.recorded = source.getRecorded();
        this.synchronizedAt = source.getSynchronizedAt();
        this.lastPhotoUploadAt = source.getLastPhotoUploadAt();
        this.place = source.getPlace();
        this.createdAt = source.getCreatedAt();
    }

    public ImageSourceShortResDTO(ImageSource source, String userId) {
        this(source);
        this.isOwner = source.isOwner(userId);
        this.url = this.isOwner ? source.getIp() : "";
        this.port = this.isOwner ? source.getPort() : null;
        this.cameraUser = this.isOwner ? source.getCameraUser() : "";
        this.cameraPassword = this.isOwner ? source.getCameraPassword() : "";
    }

}
