package com.siseth.adapter.feign.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourcePreVerificationResDTO {

    private Long imageId;

    private LocalDateTime imageCreatedAt;

    private Double lat;

    private Double lon;

    private Boolean isCorrect;

    private String algorithmName;

    private String desc;

}
