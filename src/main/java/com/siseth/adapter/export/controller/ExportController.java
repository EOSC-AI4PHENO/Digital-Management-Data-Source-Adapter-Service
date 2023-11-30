package com.siseth.adapter.export.controller;

import com.siseth.adapter.export.service.ExportService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/digital/source-adapter/export")
public class ExportController {

    private final ExportService exportService;
    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("simple/excel")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Resource> exportJsonToExcelFile(@RequestBody String jsonString) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Export.xlsx");
        ByteArrayOutputStream stream = exportService.exportJsonToExcel(jsonString);
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                header, HttpStatus.CREATED);
    }

    @GetMapping("simple/excel/meteoData/byStation")
    @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<Resource> generateExcelWithValuesByStation(@RequestParam(name = "stationId") Long stationId,
                                                                        @RequestParam(required = false, name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                        @RequestParam(required = false, name = "endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                                        @Parameter(hidden = true) @RequestHeader String id,
                                                                        @Parameter(hidden = true) @RequestHeader String realm) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Export.xlsx");
        LocalDateTime now = LocalDateTime.now();
        if (startTime == null) {
            startTime = LocalDateTime.of(0, 1, 1, 0, 0); // Assuming a default start date of year 0000
        }

        if (endTime == null) {
            endTime = now;
        }
        byte[] bytes = exportService.generateMeteoDataExcel(stationId, startTime, endTime, id, realm);
        return new ResponseEntity<>(new ByteArrayResource(bytes),
                header, HttpStatus.CREATED);
    }

    @GetMapping("simple/excel/photoesData/byAnalysisId")
    @SneakyThrows
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Resource> generateExcelWithValuesByAnalysisId(@RequestParam(name = "analysisId") Long analysisId,
                                                                    @Parameter(hidden = true) @RequestHeader String id,
                                                                    @Parameter(hidden = true) @RequestHeader String realm) {

        File file = exportService.generatePhotoDataExcel(analysisId, id, realm);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
