package com.siseth.adapter.dto.attribute;

import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class AttributeMainResDTO {

    private List<AttributeResDTO> recordedType;

    private List<AttributeResDTO> types;

    public AttributeMainResDTO() {
        this.recordedType = ImageSource.SourceRecorded.stream()
                                    .map(x -> new AttributeResDTO(x.getDesc(), x.toString()))
                                    .collect(Collectors.toList());
        this.types = ImageSource.SourceType.stream()
                                    .map(x -> new AttributeResDTO(x.getDesc(), x.toString()))
                                    .collect(Collectors.toList());
    }

}
