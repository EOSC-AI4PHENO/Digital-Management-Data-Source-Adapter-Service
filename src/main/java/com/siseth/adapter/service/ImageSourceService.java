package com.siseth.adapter.service;

import com.siseth.adapter.component.dahua.DahuaConfig;
import com.siseth.adapter.component.date.DateCreator;
import com.siseth.adapter.component.date.DateFormatter;
import com.siseth.adapter.component.file.FileUtil;
import com.siseth.adapter.component.paggingSorting.SortingConverter;
import com.siseth.adapter.dto.camera.response.FilesCameraResDTO;
import com.siseth.adapter.dto.config.request.SourceDateNameConfigReqDTO;
import com.siseth.adapter.dto.config.response.DataSourceConfigResDTO;
import com.siseth.adapter.dto.file.FileDahuaShortResDTO;
import com.siseth.adapter.dto.file.FileDownloadReqDTO;
import com.siseth.adapter.dto.source.response.SourceInternalShortResDTO;
import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.entity.source.SourceConfig;
import com.siseth.adapter.feign.analysis.AnalysisService;
import com.siseth.adapter.feign.dahua.DahuaService;
import com.siseth.adapter.feign.dahua.DahuaServiceImpl;
import com.siseth.adapter.feign.dahua.dto.*;
import com.siseth.adapter.feign.fedora.FedoraServiceImpl;
import com.siseth.adapter.feign.fedora.dto.FedoraFileExistsDTO;
import com.siseth.adapter.feign.fedora.dto.FileShortResDTO;
import com.siseth.adapter.feign.user.UserServiceImpl;
import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import com.siseth.adapter.feign.weatherStation.WeatherStationService;
import com.siseth.adapter.feign.weatherStation.WeatherStationServiceImpl;
import com.siseth.adapter.feign.weatherStation.dto.source.request.ImageSourceConfigReqDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.request.ImageSourceReqDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.*;
import com.siseth.adapter.repository.source.ImageSourceRepository;
import com.siseth.adapter.repository.source.SourceConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSourceService {

    private final ImageSourceRepository repository;

    private final SourceConfigRepository configRepository;

    private final DahuaServiceImpl dahuaService;

    private final FedoraServiceImpl fedoraService;

    private final UserServiceImpl userService;

    private final WeatherStationServiceImpl weatherStationService;

    private final AnalysisService analysisService;

    public ImageSourceResDTO getSource(Long id, String userId, String realm){
        ImageSource source = repository.findByIdAndRealm(id, realm)
                .orElseThrow(() -> new RuntimeException("Entity not found!"));
        UserInfoDTO userInfoDTO = userService.getUserInfo(source.getUserId(), source.getRealm());
        WeatherStationShortResDTO weatherStationDTO = source.getStationId() != null ?
                                                        weatherStationService.findById(source.getStationId()) :
                                                        null;

        Boolean isIncremental = analysisService.isIncremental(id, userId, realm);

        return new ImageSourceResDTO(source, userId, userInfoDTO, weatherStationDTO, isIncremental);
    }

    public ImageSourceResDTO getSource(Long id){
        ImageSource source = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found!"));
        UserInfoDTO userInfoDTO = userService.getUserInfo(source.getUserId(), source.getRealm());


        return new ImageSourceResDTO(source, source.getUserId(), userInfoDTO, null);
    }

    public SourceInternalShortResDTO getSourceInternal(Long id){
        ImageSource source = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found!"));
        return new SourceInternalShortResDTO(source);
    }

    public void updateSnapConfig(Long id, String days) {
        ImageSource source = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found!"));
        source.addOrUpdateConfig(SourceConfig.ConfigType.DAYS, days);
        repository.save(source);
    }

    public List<SourceInternalShortResDTO> getByIds(List<Long> ids) {
        return repository.findAllByIdIn(ids)
                .stream()
                .map(SourceInternalShortResDTO::new)
                .collect(Collectors.toList());
    }

    public ImageSourceShortResDTO getById(Long id) {

        return new ImageSourceShortResDTO(repository.findById(id).orElseThrow( () -> new EntityNotFoundException("Image source not found")));

    }

    public List<ImageSourceShortResDTO> getSourcesByType(ImageSource.SourceType type){
        return repository.findAllByTypeAndIsActiveIsTrue(type)
                .stream()
                .map(ImageSourceShortResDTO::new)
                .collect(Collectors.toList());
    }
    public List<ImageSourceShortResDTO> getSourcesByType(ImageSource.SourceType type, String realm){
        return repository.findAllByTypeAndRealmAndIsActiveIsTrue(type, realm)
                .stream()
                .map(ImageSourceShortResDTO::new)
                .collect(Collectors.toList());
    }

    public List<ImageSourceWithCoordinateResDTO> getAllSourcesWithCoordinates(String userId, String realm) {
        return repository.findAllByRealm(realm)
                .stream()
                .filter(x -> x.getLatitude() != null && x.getLongitude() != null)
                .map(ImageSourceWithCoordinateResDTO::new)
                .collect(Collectors.toList());
    }

    public ImageSourceWithPageResDTO getSources(String userId,
                                                String realm,
                                                Integer page,
                                                Integer size,
                                                String sorting,
                                                LocalDate dateFrom,
                                                LocalDate dateTo,
                                                String _type,
                                                String _recordedType,
                                                Boolean containsFiles,
                                                Boolean showAll,
                                                String filter) {
        Sort sort = SortingConverter.convert(sorting);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<ImageSource.SourceType> types =
                                _type != null ?
                                    Arrays.stream(_type.split(","))
                                            .map(ImageSource.SourceType::valueOf)
                                            .collect(Collectors.toList()) :
                                    List.of(ImageSource.SourceType.values());
        List<ImageSource.SourceRecorded> recordedType =
                _recordedType != null ?
                        Arrays.stream(_recordedType.split(","))
                                                .map(ImageSource.SourceRecorded::valueOf)
                                                .collect(Collectors.toList()) :
                        List.of(ImageSource.SourceRecorded.values());

        List<Long> sourceIds = containsFiles ?
                                fedoraService.getSourcesIds(realm, dateFrom, dateTo) :
                                List.of(0L);

        Page<ImageSource> pageImageSource =
                repository.findAllByUserIdAndRealmAndIsActiveIsTrue(
                        showAll ? "" : userId,
                        realm, filter,
                        types, recordedType,
                        sourceIds,
                        pageable );
        return new ImageSourceWithPageResDTO(pageImageSource, userId);
    }

    public void checkConnect(Long sourceId, String realm, ConnectDataDTO connectDataDTO) {
        ImageSource source = get(sourceId, realm);
        source.updateConfig(connectDataDTO);
        getCurrentTimeFromCamera(source);
        setSerialNumber(source);
        DahuaConfig.checkType(source.getType(), source.getDeviceType());
        repository.save(source);
    }

    public List<ImageSourceShortResDTO> getAllSources(String realm) {
        return repository.findAllByRealm(realm)
                .stream()
                .map(ImageSourceShortResDTO::new)
                .collect(Collectors.toList());
    }
    public Map<String, Object> addNewImage(Long sourceId, String userId, MultipartFile zipFile, String _dateNameRegex) {
        ImageSource imageSource = repository.findById(sourceId)
                .orElseThrow( () -> new EntityNotFoundException("Image source not found"));

        imageSource.checkUser(userId);

        String dateNameRegex = Optional.ofNullable(_dateNameRegex)
                                        .orElse(imageSource.getDateNameRegex());
        Long sendCount = 0L;
        Long sendOmitted = 0L;
        List<String> responseFiles = new ArrayList<>();
        List<String> omittedFiles = new ArrayList<>();
        try (InputStream inputStream = zipFile.getInputStream()) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    String fileName = new File(entryName).getName();
                    String directory = entryName.replace("/" + fileName, "").replace(fileName, "")
                                    .replaceAll(" ", "_");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    LocalDateTime fileCreatedAt = getFileCreationTime(fileName, dateNameRegex);
                    if (fileCreatedAt != null) {
                        FedoraFileExistsDTO fedoraDTO =
                                fedoraService.uploadFile(imageSource, FileUtil.convert(entryName, byteArrayOutputStream),
                                fileName, directory, fileCreatedAt);
                        if(fedoraDTO != null && !fedoraDTO.getIsExist())
                            sendCount ++;
                        else if(fedoraDTO != null && fedoraDTO.getIsExist()) {
                            sendOmitted++;
                            omittedFiles.add(fileName);
                        }
                    }else {
                        responseFiles.add(fileName);
                    }
                    zipInputStream.closeEntry();
                }
            }
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Map.of("sent", sendCount,
                        "omit", sendOmitted,
                        "rejected", responseFiles,
                        "omitted", omittedFiles);
    }

    private LocalDateTime getFileCreationTime(String fileName, String dateNameRegex) {
        try{
            DateCreator dateCreator = new DateCreator().createFromNameRegex(fileName, dateNameRegex);
            return dateCreator.create();
        } catch (Exception e) {
            return null;
        }
    }

    @SneakyThrows
    public ImageSourceResDTO addNewCamera(ImageSourceReqDTO camera, String userId, String realm) {

        boolean exists = repository
                .existsByUserIdAndNameAndRealmAndIsActiveIsTrue(userId, camera.getName(), realm);

        if(exists)
            throw new RuntimeException("A image source with the given name already exists!");

        WeatherStationShortResDTO stationDTO = getEdwinStation(camera.getEdwinId());


        ImageSource imageSource = new ImageSource(camera, stationDTO, userId, realm);

        return new ImageSourceResDTO(repository.save(imageSource));
    }

    public ImageSourceResDTO updateCamera(ImageSourceReqDTO camera, Long id, String userId, String roles, String realm) {
        ImageSource imageSource = repository.findById(id).orElseThrow( () -> new EntityNotFoundException("Image source not found"));
        if(!roles.contains("Admin"))
            imageSource.checkUser(userId);
        WeatherStationShortResDTO stationDTO = getEdwinStation(camera.getEdwinId());

        imageSource.update(camera, stationDTO, imageSource.getUserId(), realm);

        setSerialNumber(imageSource);

        return new ImageSourceResDTO(repository.save(imageSource));
    }

    public LocalDateTime updateDateConfig(SourceDateNameConfigReqDTO config, String userId, String realm) {
        ImageSource imageSource = get(config.getId(), realm);
        imageSource.checkUser(userId);

        if(imageSource.getFtpConfig() != null)
            throw new RuntimeException("Source has FTP Config");

        LocalDateTime dateTime = LocalDateTime.now();
        if(config.getName() != null) {
            dateTime = getFileCreationTime(config.getName(), config.getRegex());
        }

        imageSource.setDateNameRegex(config.getRegex());
        repository.save(imageSource);


        return dateTime;
    }

    public List<FileDahuaShortResDTO> getImagesFromDahua(Long sourceId, LocalDate dateFrom, LocalDate dateTo, String userId, String realm) {
        ImageSource source = get(sourceId, realm);
        String from = DateFormatter.formatDahua(dateFrom);
        String to = DateFormatter.formatDahua(dateTo.plusDays(1));

        source.checkUser(userId);

        List<FileDahuaShortResDTO> response = new ArrayList<>();
        DahuaFilesDTO dahuaFilesDTO = null;
        int i = 0;
        int maxIter = 10;
        do {
            dahuaFilesDTO =  dahuaService.getFiles(source.getId(), from, to);
            List<LocalDateTime> times = dahuaFilesDTO.getStartTimes();
            Map<LocalDateTime, Boolean> map = fedoraService.checkTimesToSource(sourceId, times);

            response.addAll(
                    dahuaFilesDTO.getFiles()
                            .stream()
                            .map(x -> new FileDahuaShortResDTO(x, map.get(DateFormatter.formatDefault(x.getStartTime()))))
                            .collect(Collectors.toList())
                             );
            from = dahuaFilesDTO.getLastStartTime();
            i++;
        } while(dahuaFilesDTO.getCount() > 99 && i < maxIter);

     return response;
    }

    @SneakyThrows
    @Async
    public void downloadFileFromCamera(Long sourceId, List<FileDownloadReqDTO> files, String userId, String realm) {
        ImageSource source = get(sourceId, realm);
        source.checkUser(userId);
        List<String> addedFiles = new ArrayList<>();
        List<String> rejectedFiles = new ArrayList<>();
        List<String> omittedFiles = new ArrayList<>();

        for (FileDownloadReqDTO fileDTO : files) {
            String[] part = fileDTO.getFilePath().split("/");
            String name = part[part.length - 1];

            String directory = fileDTO.getFilePath().replace(name, "");

            LocalDateTime dateTime = DateFormatter.formatDefault(fileDTO.getStartTime());

            byte[] file = dahuaService.getFile(source.getId(), fileDTO.getFilePath());
            MultipartFile multipartFile = FileUtil.convert(file, name);

            if(multipartFile != null) {
                log.info("Send file {} to Fedora from source {}", fileDTO.getFilePath(), sourceId);
                FedoraFileExistsDTO dto = fedoraService.uploadFile(source, multipartFile, name, directory, dateTime);
                if (dto.getIsExist())
                    omittedFiles.add(fileDTO.getFilePath());
                if(!dto.getIsExist())
                    addedFiles.add(fileDTO.getFilePath());
            } else {
                rejectedFiles.add(fileDTO.getFilePath());
            }

        }
//        return new FilesCameraResDTO(addedFiles, rejectedFiles, omittedFiles);
    }


    public DataSourceConfigResDTO getConfig(Long id) {
        ImageSource source = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found!"));
        Boolean isEnableFromCamera = getFtpConfigFromCamera(source); 
        if(isEnableFromCamera != null) {
            source.addFTPConfig(isEnableFromCamera);
            source = repository.save(source);
        }
        return Optional.ofNullable(source.getFtpConfig())
                .map(DataSourceConfigResDTO::new)
                .orElse(new DataSourceConfigResDTO());
    }

    @Transactional
    public ImageSourceResDTO updateConfig(Long id, String userId, ImageSourceConfigReqDTO api) {
        ImageSource source = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found!"));
        source.checkUser(userId);

        boolean ftpConfigExists = source.getFtpConfig() != null;

        source.updateConfig(api);

        source.addFTPConfig(api.getEnable());
        setSerialNumber(source);
        DahuaConfig.checkType(source.getType(), source.getDeviceType());
        String result = setFTPConfig(source, api.getEnable(), ftpConfigExists);
        if(result == null || result.equals(""))
            throw new RuntimeException("Cannot connect to camera!");
        return new ImageSourceResDTO(repository.save(source));
    }



    public void deleteCamera(Long id, String userId, String roles, String realm) {
        ImageSource imageSource = repository.findByIdAndRealm(id, realm).orElseThrow( () -> new EntityNotFoundException("Image source not found"));
        if(!imageSource.isOwner(userId) && !roles.contains("Admin"))
            throw new RuntimeException("Cannot delete source!");
        imageSource.delete();
        repository.save(imageSource);
    }

    public List<ImageSourceShortResDTO> getAllSourcesWithWeatherStation() {
        return repository.findAllByStationIdIsNotNull()
                .stream()
                .map(ImageSourceShortResDTO::new)
                .collect(Collectors.toList());
    }

    private Boolean getFtpConfigFromCamera(ImageSource source) {
        NasConfigDTO dto = dahuaService.getMediaEncode(source.getIp(), source.getPort(),
                                                        source.getCameraUser(), source.getCameraPassword());
        if(dto == null)
            return null;
        return dto.isFTPEnable();
    }

    private String getCurrentTimeFromCamera(ImageSource source) {
        String datetime = dahuaService.getCurrentDate(source.getIp(),
                                                        source.getPort(),
                                                        source.getCameraUser(),
                                                        source.getCameraPassword());
        if(datetime == null)
            throw new RuntimeException("Cannot connect to camera!");
        return datetime;
    }

    private ImageSource get(Long sourceId, String realm) {
        return repository.findByIdAndRealm(sourceId, realm)
                .orElseThrow(() -> new RuntimeException("Entity not found!"));
    }

    private WeatherStationShortResDTO getEdwinStation(String edwinId) {
        return edwinId != null ?
                weatherStationService.saveWeatherStations(edwinId) :
                null;
    }

    private void setSerialNumber(ImageSource source) {
        if(source.getIp() != null && (source.getSerialNumber() == null || source.getDeviceType() == null)) {
            DeviceTypeDTO.DeviceTypeItemDTO deviceTypeItemDTO = getSerialNumber(source);
            source.setSerialNumber(deviceTypeItemDTO.getSerialNumber());
            source.setDeviceType(deviceTypeItemDTO.getDeviceType());
        }
    }

    private DeviceTypeDTO.DeviceTypeItemDTO getSerialNumber(ImageSource source) {
        DeviceTypeDTO deviceType = dahuaService.getDeviceTypeInfo(source.getIp(), source.getPort(),
                                                                    source.getCameraUser(),source.getCameraPassword());
        return deviceType.getInfo();
    }

    private String setFTPConfig(ImageSource source, Boolean enable, Boolean configExists) {
        if(source.getIp() == null)
            return "";
       return dahuaService.setMediaEncode(source.getIp(), source.getPort(),
                                    source.getCameraUser(),source.getCameraPassword(),
                                    new SetNasConfigDTO(source, enable, configExists));
    }

    public ImageSource getSourceEntity(Long imageSourceId) {
        return repository.findById(imageSourceId)
                .orElse(null);
    }
}
