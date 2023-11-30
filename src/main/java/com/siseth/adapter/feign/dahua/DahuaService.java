package com.siseth.adapter.feign.dahua;

import com.siseth.adapter.feign.dahua.dto.*;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@FeignClient(name = "camera-service")
public interface DahuaService {

    @GetMapping("/api/internal/dahua/time/timezone")
    String getTimezone(@RequestParam String ip,
                       @RequestParam Integer port,
                       @RequestParam String user,
                       @RequestParam String pass);

    @GetMapping("/api/internal/dahua/time")
    String getCurrentDate(@RequestParam String ip,
                           @RequestParam Integer port,
                           @RequestParam String user,
                           @RequestParam String pass);

    @PostMapping("/api/internal/dahua/time/timezone")
    String setTimezone(@RequestParam String ip,
                       @RequestParam Integer port,
                       @RequestParam String user,
                       @RequestParam String pass,
                       @RequestParam String timezone);

    @GetMapping("/api/internal/dahua/device/type")
    DeviceTypeDTO getDeviceTypeInfo(@RequestParam String ip,
                                    @RequestParam Integer port,
                                    @RequestParam String user,
                                    @RequestParam String pass);

    @GetMapping("/api/internal/dahua/device/info")
    List<DeviceInfoItemDTO> getDeviceAllInfo(@RequestParam String ip,
                                              @RequestParam Integer port,
                                              @RequestParam String user,
                                              @RequestParam String pass);

    @PutMapping("/api/internal/dahua/media/nas")
    String setMediaEncode(@RequestParam String ip,
                           @RequestParam Integer port,
                           @RequestParam String user,
                           @RequestParam String pass,
                           @RequestBody SetNasConfigDTO dto);

    @GetMapping("/api/internal/dahua/media/nas")
    NasConfigDTO getMediaEncode(@RequestParam String ip,
                                @RequestParam Integer port,
                                @RequestParam String user,
                                @RequestParam String pass);

    @PostMapping(value = "/api/digital/dahua/files/{sourceId}/download")
    byte[] getFile(@PathVariable Long sourceId,
                                @RequestParam String filename);


    @GetMapping("/api/digital/dahua/files/{sourceId}")
    DahuaFilesDTO getFiles(@PathVariable Long sourceId,
                           @RequestParam String from,
                           @RequestParam String to);


}
