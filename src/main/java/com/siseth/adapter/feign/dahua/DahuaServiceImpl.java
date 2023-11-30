package com.siseth.adapter.feign.dahua;

import com.siseth.adapter.feign.dahua.dto.*;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DahuaServiceImpl {

    private final DahuaService dahuaService;

    public String getTimezone(String ip, Integer port, String user, String pass) {
        try {
            return dahuaService.getTimezone(ip, port, user, pass);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public String setTimezone(String ip, Integer port, String user, String pass, String timezone) {
        try {
            return dahuaService.setTimezone(ip, port, user, pass, timezone);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public DeviceTypeDTO getDeviceTypeInfo(String ip, Integer port, String user, String pass) {
        try {
            return dahuaService.getDeviceTypeInfo(ip, port, user, pass);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public List<DeviceInfoItemDTO> getDeviceAllInfo(String ip, Integer port, String user, String pass) {
        try {
            return dahuaService.getDeviceAllInfo(ip, port, user, pass);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public byte[] getFile(Long sourceId, String filename) {
        try {
            return dahuaService.getFile(sourceId, filename);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public DahuaFilesDTO getFiles(Long sourceId,
                                  String from,
                                  String to)
    {
        try {
            return dahuaService.getFiles(sourceId, from, to);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public String setMediaEncode(String ip,
                                 Integer port,
                                 String user,
                                 String pass,
                                 SetNasConfigDTO dto)
    {
        try {
            return dahuaService.setMediaEncode(ip, port, user, pass, dto);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public NasConfigDTO getMediaEncode(String ip,
                                       Integer port,
                                       String user,
                                       String pass)
    {
        try {
            return dahuaService.getMediaEncode(ip, port, user, pass);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }

    public String getCurrentDate(String ip,
                                 Integer port,
                                 String user,
                                 String pass)
    {
        try {
            return dahuaService.getCurrentDate(ip, port, user, pass);
        } catch (Exception e) {
            log.error("Error when try connect to Dahua-Service, message {}", e.getMessage());
            return null;
        }
    }


}
