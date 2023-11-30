package com.siseth.adapter.feign.analysis;

import com.siseth.adapter.feign.analysis.dto.AnalysisParameterResDTO;
import com.siseth.adapter.feign.analysis.dto.AnalysisResDTO;
import com.siseth.adapter.feign.analysis.dto.SourcePreVerificationResDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(name = "analysis-service")
public interface AnalysisService {

    @GetMapping("/api/image-processing/analysis/report/getRawData")
    public ResponseEntity<List<Map<String, Object>>> getRawData(@RequestParam Long analysisId,
                                                                @RequestHeader String id,
                                                                @RequestHeader String realm);

    @GetMapping("/api/image-processing/analysis/report/getRawData/byImageId")
    public ResponseEntity<List<Map<String, Object>>> getRawDataForImageId(@RequestParam Long imageId,
                                                                          @RequestHeader String id,
                                                                          @RequestHeader String realm);
    @GetMapping("api/internal/image-processing/analysis/report/getParameters")
    public ResponseEntity<List<AnalysisParameterResDTO>> getParameters(@Parameter(hidden = true) @RequestHeader String id,
                                                                       @Parameter(hidden = true)  @RequestHeader String realm);
    @GetMapping("/api/image-processing/analysis/report/getSourcePreVer/byImageId")
    public ResponseEntity<SourcePreVerificationResDTO> getSourcePreVerForImageId(@RequestParam Long imageId,
                                                                                 @RequestHeader String id,
                                                                                 @RequestHeader String realm);

    @GetMapping("/api/image-processing/analysis/getAnalysis")
    public ResponseEntity<AnalysisResDTO> getAnalysis(@RequestParam Long analysisId,
                                                      @Parameter(hidden = true) @RequestHeader String id,
                                                      @Parameter(hidden = true) @RequestHeader String realm);

    @GetMapping("api/internal/image-processing/analysis/incremental")
    Boolean isIncremental(@RequestParam Long sourceId,
                          @RequestHeader String id,
                          @RequestHeader String realm);
}
