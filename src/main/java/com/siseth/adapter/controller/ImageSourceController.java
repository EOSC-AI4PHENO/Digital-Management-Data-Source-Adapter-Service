package com.siseth.adapter.controller;

import com.siseth.adapter.dto.attribute.AttributeMainResDTO;
import com.siseth.adapter.dto.camera.response.FilesCameraResDTO;
import com.siseth.adapter.dto.config.request.SourceDateNameConfigReqDTO;
import com.siseth.adapter.dto.config.response.DataSourceConfigResDTO;
import com.siseth.adapter.dto.file.FileDahuaShortResDTO;
import com.siseth.adapter.dto.file.FileDownloadReqDTO;
import com.siseth.adapter.feign.dahua.dto.ConnectDataDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.request.ImageSourceConfigReqDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.request.ImageSourceReqDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.ImageSourceResDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.ImageSourceWithCoordinateResDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.ImageSourceWithPageResDTO;
import com.siseth.adapter.service.ImageSourceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/digital/source-adapter")
public class ImageSourceController {

    private final ImageSourceService service;

    @GetMapping("/attribute")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AttributeMainResDTO> getAttribute() {
        return ResponseEntity.ok(new AttributeMainResDTO());
    }

    @GetMapping("/{sourceId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceResDTO> getSource(@PathVariable Long sourceId,
                                                       @Parameter(hidden = true)  @RequestHeader String id,
                                                       @Parameter(hidden = true) @RequestHeader(required = false) String realm){
        return ResponseEntity.ok(service.getSource(sourceId, id, realm));
    }

    @GetMapping("/getUserSources")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceWithPageResDTO> getUserSources( @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                     @RequestParam(required = false, defaultValue = "100000") Integer size,
                                                                     @RequestParam(required = false, defaultValue = "") String sort,
                                                                     @RequestParam(required = false, defaultValue = "") String filter,
                                                                     @RequestParam(required = false, defaultValue = "1900-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                                     @RequestParam(required = false, defaultValue = "2100-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateTo,
                                                                     @RequestParam(required = false) String type,
                                                                     @RequestParam(required = false) String recordedType,
                                                                     @RequestParam(required = false, defaultValue = "false") Boolean containsFiles,
                                                                     @Parameter(hidden = true)  @RequestHeader String id,
                                                                     @Parameter(hidden = true)  @RequestHeader String realm){
        return ResponseEntity.ok(service.getSources(id, realm, page, size, sort, dateFrom, dateTo, type, recordedType, containsFiles, false, filter));
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceWithPageResDTO> getAllSources( @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                     @RequestParam(required = false, defaultValue = "100000") Integer size,
                                                                     @RequestParam(required = false, defaultValue = "") String sort,
                                                                     @RequestParam(required = false, defaultValue = "") String filter,
                                                                     @RequestParam(required = false, defaultValue = "1900-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                                     @RequestParam(required = false, defaultValue = "2100-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateTo,
                                                                     @RequestParam(required = false) String type,
                                                                     @RequestParam(required = false) String recordedType,
                                                                     @RequestParam(required = false, defaultValue = "false") Boolean containsFiles,
                                                                     @Parameter(hidden = true)  @RequestHeader String id,
                                                                     @Parameter(hidden = true)  @RequestHeader String realm){
        return ResponseEntity.ok(service.getSources(id, realm, page, size, sort, dateFrom, dateTo, type, recordedType, containsFiles,true, filter));
    }

    @GetMapping("/coordinates/all")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<ImageSourceWithCoordinateResDTO>> getAllSourcesWithCoordinates(
                                                                    @Parameter(hidden = true)  @RequestHeader(required = false) String id,
                                                                    @Parameter(hidden = true)  @RequestHeader String realm){
        return ResponseEntity.ok(service.getAllSourcesWithCoordinates(id, realm));
    }

    @PostMapping("/connect")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> testConnection(@RequestBody ConnectDataDTO dto,
                                            @RequestParam Long sourceId,
                                            @Parameter(hidden = true) @RequestHeader String realm){
        service.checkConnect(sourceId, realm, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addNewCamera")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceResDTO> addNewCamera(@RequestBody ImageSourceReqDTO camera,
                                               @Parameter(hidden = true) @RequestHeader String id,
                                               @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.addNewCamera(camera, id, realm));
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceResDTO> addNewSource(@RequestBody ImageSourceReqDTO camera,
                                                          @Parameter(hidden = true) @RequestHeader String id,
                                                          @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.addNewCamera(camera, id, realm));
    }

    @PostMapping("/addNewImage")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> addNewImage(@RequestParam("sourceId") Long sourceId,
                                                           @RequestParam(value = "dateNameRegex", required = false) String dateNameRegex,
                                                           @RequestParam("file") MultipartFile zipFile,
                                                           @Parameter(hidden = true) @RequestHeader String id,
                                                           @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.addNewImage(sourceId, id,  zipFile, dateNameRegex));
    }

    @PostMapping("/list/download")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<FileDahuaShortResDTO>> getImagesFromDahua(@RequestParam("sourceId") Long sourceId,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateTo,
                                                                         @Parameter(hidden = true) @RequestHeader String id,
                                                                         @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.getImagesFromDahua(sourceId,  dateFrom, dateTo, id, realm));
    }
    @PostMapping("/download")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<FilesCameraResDTO> downloadFile(@RequestParam("sourceId") Long sourceId,
                                                     @RequestBody List<FileDownloadReqDTO> files,
                                                     @Parameter(hidden = true) @RequestHeader String id,
                                                     @Parameter(hidden = true) @RequestHeader String realm) {
        service.downloadFileFromCamera(sourceId,  files, id, realm);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateCamera/{sourceId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceResDTO> updateCamera(@RequestBody ImageSourceReqDTO camera, @PathVariable Long sourceId,
                                                          @Parameter(hidden = true) @RequestHeader String id,
                                                          @Parameter(hidden = true) @RequestHeader(required = false, defaultValue = "") String roles,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.updateCamera(camera, sourceId, id, roles, realm));
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ImageSourceResDTO> updateSource(@RequestBody ImageSourceReqDTO camera,
                                                          @RequestParam Long id,
                                                          @Parameter(hidden = true) @RequestHeader(name = "id") String userId,
                                                          @Parameter(hidden = true) @RequestHeader(required = false, defaultValue = "") String roles,
                                                          @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.updateCamera(camera, id, userId, roles, realm));
    }

    @PutMapping("/config/date")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<LocalDateTime> updateDateConfig(@RequestBody SourceDateNameConfigReqDTO config,
                                                          @Parameter(hidden = true) @RequestHeader(name = "id") String userId,
                                                          @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.updateDateConfig(config, userId, realm));
    }

    @DeleteMapping("/deleteCamera/{sourceId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> updateCamera(@PathVariable Long sourceId,
                                             @Parameter(hidden = true) @RequestHeader String id,
                                             @Parameter(hidden = true) @RequestHeader(required = false, defaultValue = "") String roles,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        service.deleteCamera(sourceId, id, roles, realm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/config/ftp")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<DataSourceConfigResDTO> getFTPConfig(@RequestParam Long id) {
        return ResponseEntity.ok(service.getConfig(id));
    }


    @PutMapping("/config")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> addCameraConfig(@Parameter(hidden = true) @RequestHeader String id,
                                                @RequestBody ImageSourceConfigReqDTO api) {
        service.updateConfig(api.getId(), id, api);
        return ResponseEntity.ok().build();
    }

}
