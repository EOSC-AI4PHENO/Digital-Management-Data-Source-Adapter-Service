package com.siseth.adapter.dto.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadReqDTO {

    private String filePath;

    private String startTime;

}
