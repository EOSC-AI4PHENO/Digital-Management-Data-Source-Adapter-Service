package com.siseth.adapter.feign.dahua.dto;

import com.siseth.adapter.component.date.DateFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DahuaFilesDTO {

    private List<DahuaFileDTO> files;

    private Integer count;

    private String lastStartTime;

    public List<LocalDateTime> getStartTimes() {
        return this.files != null ?
                this.files.stream()
                        .map(DahuaFileDTO::getStartTime)
                        .map(DateFormatter::formatDefault)
                        // TODO cut seconds
                        .collect(Collectors.toList()) :
                new ArrayList<>();
    }

}

