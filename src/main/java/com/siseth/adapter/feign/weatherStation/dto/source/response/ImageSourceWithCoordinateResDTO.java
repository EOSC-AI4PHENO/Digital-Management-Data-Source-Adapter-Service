package com.siseth.adapter.feign.weatherStation.dto.source.response;

import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceWithCoordinateResDTO {

    private Long id;
    private Double longitude;
    private Double latitude;
    private String name;
    private String desc;
    private ImageSource.SourceRecorded recorded;

    public ImageSourceWithCoordinateResDTO(ImageSource source) {
        this.id = source.getId();
        this.longitude = source.getLongitude();
        this.latitude = source.getLatitude();
        this.name = source.getName();
        this.desc = source.getDesc();
        this.recorded = source.getRecorded();
    }

}
