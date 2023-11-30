package com.siseth.adapter.export.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.export.service.entity.PhotoDataExcelObject;
import com.siseth.adapter.feign.analysis.AnalysisService;
import com.siseth.adapter.feign.analysis.dto.AnalysisParameterResDTO;
import com.siseth.adapter.feign.analysis.dto.AnalysisResDTO;
import com.siseth.adapter.feign.fedora.FedoraService;
import com.siseth.adapter.feign.fedora.dto.FileShortResDTO;
import com.siseth.adapter.feign.weatherStation.WeatherStationService;
import com.siseth.adapter.feign.weatherStation.dto.source.response.MeteoDataDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.WeatherStationDTO;
import com.siseth.adapter.service.ImageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final JsonToExcelConverter converter;
    private final ExcelMeteoDataGenerator meteoDataGenerator;
    private final ExcelPhotoDataGenerator photoDataGenerator;
    private final ImageSourceService imageSourceService;
    private final FedoraService fedoraService;
    private final WeatherStationService weatherStationService;

    private final AnalysisService analysisService;

    @Autowired
    public ExportService(JsonToExcelConverter converter, ExcelMeteoDataGenerator meteoDataGenerator,
                         ExcelPhotoDataGenerator photoDataGenerator, ImageSourceService imageSourceService, FedoraService fedoraService, WeatherStationService weatherStationService, AnalysisService analysisService) {
        this.converter = converter;
        this.meteoDataGenerator = meteoDataGenerator;
        this.photoDataGenerator = photoDataGenerator;
        this.imageSourceService = imageSourceService;
        this.fedoraService = fedoraService;
        this.weatherStationService = weatherStationService;
        this.analysisService = analysisService;
    }

    public ByteArrayOutputStream exportJsonToExcel(String jsonString) {
        JsonNode jsonData = converter.getJsonNodeFromString(jsonString);
        return converter.jsonDataToExcelFile(jsonData);
    }

    public byte[] generateMeteoDataExcel(Long stationId, LocalDateTime startTime, LocalDateTime endTime, String id, String realm) {
        WeatherStationDTO weatherStationDTO = weatherStationService.getWeatherStations(stationId);
        List<MeteoDataDTO> meteoDataDTOS = weatherStationService.getMeteoDataForStationInTime(startTime, endTime, stationId, 0 , 1000, "asc").getMeteoDataList();
        return meteoDataGenerator.generateMeteoRaportExcel(weatherStationDTO,meteoDataDTOS);
    }

    public File generatePhotoDataExcel(Long analysisId, String id, String realm) {
        PhotoDataExcelObject photoDataExcelObject = new PhotoDataExcelObject();
        AnalysisResDTO analysis = analysisService.getAnalysis(analysisId, id, realm).getBody();
        if(analysis == null)
            throw new RuntimeException("Analysis don't exist");
        if(analysis.getSourceId() == null)
            throw new RuntimeException("Analysis don't have image Source");
        ImageSource imageSource = imageSourceService.getSourceEntity(analysis.getSourceId());
        List<Map<String, Object>> rawDataForWholeAnalysis = analysisService.getRawData(analysisId, id, realm).getBody();

        List<AnalysisParameterResDTO> parameters = analysisService.getParameters(id, realm).getBody();
        if(parameters != null)
            parameters = parameters.stream().filter(a -> a.getDesc() != null && !a.getDesc().isBlank()).collect(Collectors.toList());

        if (rawDataForWholeAnalysis != null) {
            for (Map<String, Object> analysisDataForPicture : rawDataForWholeAnalysis) {
                Long imageId = getImageId(analysisDataForPicture);
                FileShortResDTO fileShortRes = fedoraService.getFilesImageDataById(imageId).getBody();
                if(fileShortRes != null && fileShortRes.getId() != null)
                    photoDataExcelObject.addAnalysisToFileData(fileShortRes, analysisDataForPicture);
            }
        }
        return photoDataGenerator.generatePhotoRaportExcel(analysis, imageSource, photoDataExcelObject, parameters);
    }

    private Long getImageId(Map<String, Object> analysisDataForPicture) {
        Object imageIdObject = analysisDataForPicture.get("imageId");
        if (imageIdObject instanceof Number) {
            return ((Number) imageIdObject).longValue();
        } else {
            throw new IllegalArgumentException("Invalid imageId in analysis data");
        }
    }
}
