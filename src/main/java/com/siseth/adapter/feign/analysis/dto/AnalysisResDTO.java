package com.siseth.adapter.feign.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResDTO {

    private Long id;
    private String name;

    private String desc;

    private AnalysisType type;

    private Long sourceId;

    private String algorithmName;

    private AnalysisStatus status;

    private LocalDateTime createdAt;

    private LocalDate dateFrom;

    private LocalDate dateTo;


    public enum AnalysisStatus {
        ACTIVE,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }

    public enum AnalysisType {
        LINDEN_BASIC,
        LINDEN_EXTENDED,
        APPLE
    }

    public String[] getDataRow() {
        return new String[]{
                String.valueOf(this.algorithmName),
                this.desc
        };
    }
}

