package com.siseth.adapter.controller;

import com.siseth.adapter.constant.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/digital/source-adapter/version")
public class VersionController {

    @GetMapping
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(AppProperties.VERSION);
    }

}
