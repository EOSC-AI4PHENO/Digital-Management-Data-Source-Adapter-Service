package com.siseth.adapter.export.service.entity;

import com.siseth.adapter.feign.fedora.dto.FileShortResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDataExcelObject {

    private List<ImageDataAndAnalysis> imageDataAndAnalyses = new ArrayList<>();
    public void addAnalysisToFileData(FileShortResDTO fileShortRes,  Map<String, Object> analysisData) {
    if(imageDataAndAnalyses == null)
        imageDataAndAnalyses = new ArrayList<>();
    imageDataAndAnalyses.add(new ImageDataAndAnalysis(fileShortRes, analysisData));
    }
}
