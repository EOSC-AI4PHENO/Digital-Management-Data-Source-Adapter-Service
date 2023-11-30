package com.siseth.adapter.feign.fedora.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileShortResDTO {

    private Long id;

    private Long sourceId;

    private String name;

    private String path;

    private String directory;

    private LocalDateTime originCreatedAt;

}
