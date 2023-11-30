package com.siseth.adapter.dto.file;

import com.siseth.adapter.feign.dahua.dto.DahuaFileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileDahuaShortResDTO {

    private String filePath;

    private String startTime;

    private String length;

    private String type;

    private Boolean isExists;

    public FileDahuaShortResDTO(DahuaFileDTO dto, Boolean isExists) {
        this.filePath = dto.getFilePath();
        this.startTime = dto.getStartTime();
        this.length = dto.getLength();
        this.type = dto.getType();
        this.isExists = isExists;
    }

}
