package com.siseth.adapter.statistics.controller;


import com.siseth.adapter.feign.fedora.dto.FilesSizeDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.ImageSourceResDTO;
import com.siseth.adapter.service.ImageSourceService;
import com.siseth.adapter.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/digital/source-adapter/statistics")
public class StatisticsController {

    private final StatisticsService service;

    @GetMapping("")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<FilesSizeDTO>> getStatistics(@RequestParam String userId,
                                                            @Parameter(hidden = true)  @RequestHeader String id,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm){
        return ResponseEntity.ok(service.getStatistics(userId, realm));
    }

    @GetMapping("/user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<FilesSizeDTO>> getUserStatistics(@Parameter(hidden = true)  @RequestHeader String id,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm){
        return ResponseEntity.ok(service.getStatistics(id, realm));
    }

}
