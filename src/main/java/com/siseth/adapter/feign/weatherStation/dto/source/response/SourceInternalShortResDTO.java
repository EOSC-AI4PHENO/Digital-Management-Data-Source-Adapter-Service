package com.siseth.adapter.dto.source.response;

import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourceInternalShortResDTO {

    private Long id;

    private String userId;

    private String name;

    public SourceInternalShortResDTO(ImageSource source) {
        this.id = source.getId();
        this.userId = source.getUserId();
        this.name = source.getName();
    }

}
