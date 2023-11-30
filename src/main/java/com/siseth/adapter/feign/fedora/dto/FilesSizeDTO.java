package com.siseth.adapter.feign.fedora.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilesSizeDTO {

    private Long sourceId;
    private String name;
    private Long filesCount;
    private Long size;


    public FilesSizeDTO setParams(ImageSource source){
        this.sourceId = source.getId();
        this.name = source.getName();
        return this;
    }
}
