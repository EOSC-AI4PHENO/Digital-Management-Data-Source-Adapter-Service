package com.siseth.adapter.feign.fedora;

import com.siseth.adapter.feign.fedora.dto.FedoraFileExistsDTO;
import com.siseth.adapter.feign.fedora.dto.FileShortResDTO;
import com.siseth.adapter.feign.fedora.dto.FilesSizeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@FeignClient(name = "fedora-management-service")
public interface FedoraService {

    @PostMapping(value = "/api/internal/assets/fedora/image", headers = "Content-Type=multipart/form-data")
    FedoraFileExistsDTO uploadFileTest(@RequestPart("file") MultipartFile file,
                                       @RequestParam String directory,
                                       @RequestParam Long sourceId,
                                       @RequestParam String name,
                                       @RequestParam LocalDateTime originCreatedAt,
                                       @RequestParam String realm,
                                       @RequestParam String userId);
    @GetMapping(value = "/api/internal/assets/fedora/file/source/{sourceId}/lastDate")
    LocalDateTime getLastPhotoDateToSourceId(@PathVariable Long sourceId);

    @GetMapping(value = "/api/internal/assets/fedora/file/source/{sourceId}/dates")
    Map<LocalDateTime, Boolean> checkTimesToSource(@PathVariable Long sourceId, @RequestBody List<LocalDateTime> times);

    @GetMapping(value = "/api/internal/assets/fedora/image/check")
    Boolean checkImage(@RequestParam String directory,
                                        @RequestParam Long sourceId,
                                        @RequestParam String name,
                                        @RequestParam String realm);

    @GetMapping(value = "/api/internal/assets/fedora/file/source/{sourceId}")
    List<FileShortResDTO> getFileImageToSource(
            @PathVariable Long sourceId,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            @RequestParam String realm);


    @GetMapping(value ="/api/internal/assets/fedora/file/img/{imgId}")
    public ResponseEntity<FileShortResDTO> getFilesImageDataById(@PathVariable Long imgId);


    @GetMapping(value = "/api/internal/assets/fedora/size")
    FilesSizeDTO getSizeByOwner(@RequestParam String owner,
                                @RequestParam String realm,
                                @RequestParam Long sourceId);

    @GetMapping(value = "/api/internal/assets/fedora/file/source/betweenDates")
    List<Long> getSourcesIds(@RequestParam String realm,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate dateFrom,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate dateTo);

}
