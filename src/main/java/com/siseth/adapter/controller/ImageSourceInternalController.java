package com.siseth.adapter.controller;

import com.siseth.adapter.dto.source.response.SourceInternalShortResDTO;
import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.weatherStation.dto.source.response.ImageSourceResDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.ImageSourceShortResDTO;
import com.siseth.adapter.service.ImageSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/digital/source-adapter")
public class ImageSourceInternalController {

    private final ImageSourceService service;

    @GetMapping("/{id}")
    public ResponseEntity<ImageSourceShortResDTO> getSource(@PathVariable Long id){
        return ResponseEntity.ok(service.getSource(id));
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<SourceInternalShortResDTO> getSourceInternal(@PathVariable Long id){
        return ResponseEntity.ok(service.getSourceInternal(id));
    }

    @GetMapping
    public ResponseEntity<List<ImageSourceShortResDTO>> getSourcesByType(@RequestParam ImageSource.SourceType type){
        return ResponseEntity.ok(service.getSourcesByType(type));
    }

    @PutMapping("/{id}/snapconfig")
    public ResponseEntity<Void> updateSnapConfig(@PathVariable Long id, @RequestParam String days){
        service.updateSnapConfig(id, days);
        return ResponseEntity.ok().build();
    }

    @GetMapping("ids")
    public ResponseEntity<List<SourceInternalShortResDTO>> getAllByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.getByIds(ids));
    }



}
