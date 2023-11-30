package com.siseth.adapter.feign.dahua.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DahuaFileDTO {

    private String filePath;

    private String startTime;

    private String length;

    private String type;


}
