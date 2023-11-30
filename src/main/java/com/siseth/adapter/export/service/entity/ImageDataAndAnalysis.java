package com.siseth.adapter.export.service.entity;

import com.siseth.adapter.constant.TimeUtils;
import com.siseth.adapter.feign.analysis.dto.AnalysisParameterResDTO;
import com.siseth.adapter.feign.analysis.dto.SourcePreVerificationResDTO;
import com.siseth.adapter.feign.fedora.dto.FileShortResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDataAndAnalysis {
    private FileShortResDTO fileShortRes;
    private Map<String, Object> analysisData;

    public String[] getDataRow(List<AnalysisParameterResDTO> parameters) {

        String[] preAnalysisPartOfDataRow ={fileShortRes.getSourceId().toString(),
                fileShortRes.getId().toString(),
                fileShortRes.getName(),
                fileShortRes.getDirectory(),
                TimeUtils.getLocalDateInStringFormat(fileShortRes.getOriginCreatedAt()),
              };

        String[] analysisPartOfDataRow = new String[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            String parameter = parameters.get(i).getName();
                if (analysisData.containsKey(parameter)) {
                    analysisPartOfDataRow[i] = analysisData.get(parameter).toString();
                }
        }

        int combinedLength = preAnalysisPartOfDataRow.length + analysisPartOfDataRow.length;
        String[] dataRow = new String[combinedLength];
        System.arraycopy(preAnalysisPartOfDataRow, 0, dataRow, 0, preAnalysisPartOfDataRow.length);
        System.arraycopy(analysisPartOfDataRow, 0, dataRow, preAnalysisPartOfDataRow.length, analysisPartOfDataRow.length);

        return dataRow;
    }
}
