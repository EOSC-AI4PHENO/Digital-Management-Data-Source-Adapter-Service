package com.siseth.adapter.feign.fedora;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.fedora.dto.FedoraFileExistsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FedoraServiceImpl {

    private final FedoraService fedoraService;

    public FedoraFileExistsDTO uploadFile(ImageSource source, MultipartFile file,
                                          String name, String directory,
                                          LocalDateTime originCreatedAt) {
        try {
            return fedoraService.uploadFileTest(file, directory,
                                                source.getId(), name,
                                                originCreatedAt,
                                                source.getRealm(), source.getUserId());



        } catch (Exception e) {
            log.error("Error when try connect to Fedora-Service, message {}", e.getMessage());
            return null;
        }
    }




    public List<Long> getSourcesIds(String realm,
                                    LocalDate dateFrom,
                                    LocalDate dateTo) {
        try {
            return fedoraService.getSourcesIds(realm, dateFrom, dateTo);
        } catch (Exception e) {
            log.error("Error when try connect to Fedora-Service, message {}", e.getMessage());
            return List.of(0L);
        }
    }

    public Map<LocalDateTime, Boolean> checkTimesToSource(Long sourceId,
                                    List<LocalDateTime> times) {
        try {
            return fedoraService.checkTimesToSource(sourceId, times);
        } catch (Exception e) {
            log.error("Error when try connect to Fedora-Service, message {}", e.getMessage());
            return new HashMap<>();
        }
    }




}
