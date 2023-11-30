package com.siseth.adapter.entity.source;

import com.siseth.adapter.component.dahua.DahuaConfig;
import com.siseth.adapter.component.entity.BaseEntity;
import com.siseth.adapter.component.string.StringUtil;
import com.siseth.adapter.feign.dahua.dto.ConnectDataDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.request.ImageSourceConfigReqDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.request.ImageSourceReqDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.WeatherStationShortResDTO;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.siseth.adapter.entity.source.ImageSource.SourceType.getDahuaType;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "source", name = "`D_IMAGE_SOURCE`")
@Where(clause = "`isActive`")
public class ImageSource extends BaseEntity {

    @Column
    private String name;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "`realm`")
    private String realm;

    @Column
    private String ip;

    @Column
    private Integer port;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "`recorded`")
    private SourceRecorded recorded;

    @Column(name = "`cameraUser`")
    private String cameraUser;

    @Column(name = "`cameraPassword`")
    private String cameraPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "`type`")
    private SourceType type;

    @Column(name = "`serialNumber`")
    private String serialNumber;

    @Column(name="`deviceType`")
    private String deviceType;

    @Column(name = "`directory`")
    private String directory;

    @Column(name = "`directoryRegex`")
    private String directoryRegex;

    @Column(name="`dateNameRegex`")
    private String dateNameRegex;

    @Column(name="`isActive`")
    private Boolean isActive;

    @Column(name="`synchronize`")
    private Boolean synchronize;

    @Column(name="`userId`")
    private String userId;

    @Column(name = "`stationId`")
    private Long stationId;

    @Column(name = "`producer`")
    private String producer;

    @Column(name = "`zoom`")
    private String zoom;

    @Column(name = "`place`")
    private String place;

    @Column(name = "`installationAt`")
    private OffsetDateTime installationAt;

    @Column(name = "`synchronizedAt`")
    private OffsetDateTime synchronizedAt;

    @Column(name = "`lastPhotoUploadAt`")
    private OffsetDateTime lastPhotoUploadAt;


    @Column
    private Integer azimuth;

    @Column
    private Integer inclination;

    @OneToMany(mappedBy = "source", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<SourceConfig> configList;


    public enum SourceType {
        DAHUA_5 ("Camera Dahua 5 Mpx"),
        DAHUA_8 ("Camera Dahua 8 Mpx"),
        DAHUA_12 ("Camera Dahua 12 Mpx"),

        LOCAL("Local");

        @Getter
        private String desc;

        SourceType(String desc) {
            this.desc = desc;
        }

        public static Stream<SourceType> stream() {
            return Stream.of(SourceType.values());
        }

        public static List<SourceType> getDahuaType() {
            return Arrays.asList(DAHUA_5, DAHUA_8, DAHUA_12);
        }

        public static List<SourceType> getLocalType() {
            return List.of(LOCAL);
        }

    }

    public enum SourceRecorded {
        LINDEN("Linden tree"),
        APPLE("Apple");

        @Getter
        private String desc;

        SourceRecorded(String desc) {
            this.desc = desc;
        }

        public static Stream<SourceRecorded> stream() {
            return Stream.of(SourceRecorded.values());
        }

    }

    public void sync() {
        this.synchronizedAt = OffsetDateTime.now();
    }

    public void setPhotoSync() {
        this.lastPhotoUploadAt = OffsetDateTime.now();
    }

    public void delete() {
        this.isActive = false;
    }

    public SourceConfig getConfig(SourceConfig.ConfigType type) {
        return Optional
                .ofNullable(this.configList)
                .flatMap(x -> x.stream()
                        .filter(y -> y.getType().equals(type))
                        .findAny())
                .orElse(null);
    }

    public SourceConfig getFtpConfig() {
        return getConfig(SourceConfig.ConfigType.FTP);
    }

    public void addOrUpdateConfig(SourceConfig.ConfigType type, String days) {
        SourceConfig config = getConfig(type);
        if(config == null)
            config = new SourceConfig().create(days, this);
        config.setObj(days);
        configList.add(config);
        sync();
    }

    public ImageSource(ImageSourceReqDTO dto, WeatherStationShortResDTO stationDTO,
                       String userId, String realm) {
        this.update(dto, stationDTO, userId, realm);
    }

    public void checkUser(String userId) {
        if(!this.userId.equals(userId))
            throw new RuntimeException("User is not an owner!");
    }
    public void updateConfig(ImageSourceConfigReqDTO api) {
        this.ip = Optional.ofNullable(this.ip).orElse(api.getUrl());
        this.port = Optional.ofNullable(this.port).orElse(api.getPort());
        this.cameraUser = Optional.ofNullable(this.cameraUser).orElse(api.getCameraUser());
        this.cameraPassword = Optional.ofNullable(this.cameraPassword).orElse(api.getCameraPassword());
        checkValid();
    }

    public void updateConfig(ConnectDataDTO api) {
        this.ip = Optional.ofNullable(this.ip).orElse(api.getUrl());
        this.port = Optional.ofNullable(this.port).orElse(api.getPort());
        this.cameraUser = Optional.ofNullable(this.cameraUser).orElse(api.getUser());
        this.cameraPassword = Optional.ofNullable(this.cameraPassword).orElse(api.getPassword());
        checkValid();
    }

    private void checkValid() {
        if (this.ip == null || this.port == null || this.cameraUser == null || this.cameraPassword == null) {
            throw new RuntimeException("Wrong parameters!");
        }
    }

    public void update(ImageSourceReqDTO dto, WeatherStationShortResDTO stationDTO,
                       String userId, String realm) {
        this.name = dto.getName();
        this.desc = dto.getDesc();
        if(this.type == null)
            this.type = dto.getType();
        if(this.directory == null && getDahuaType().contains(this.type))
            this.directory = StringUtil.generateLetters(8);
        this.directoryRegex = DahuaConfig.getDirectoryRegex(this.type);
        this.dateNameRegex = DahuaConfig.getDateNameRegex(this.type);
        this.serialNumber = dto.getSerialNumber();
        this.isActive = true;
        this.synchronize = Optional.ofNullable(this.synchronize)
                                                .orElse(false);
        if(this.userId == null)
            this.userId = userId;
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.recorded = dto.getRecordedType();
        if(this.realm == null)
            this.realm = realm;
        this.producer = dto.getProducer();
        this.zoom = dto.getZoom();
        this.place = dto.getPlace();
        this.installationAt = dto.getInstallationAt();
        this.azimuth = dto.getAzimuth();
        this.inclination = dto.getInclination();
        if (this.configList == null)
            this.configList = new ArrayList<>();
        this.stationId = Optional.ofNullable(stationDTO)
                                    .map(WeatherStationShortResDTO::getStationId)
                                    .orElse(null);
    }

    public void addConfig(SourceConfig config) {
        if(this.configList == null)
            this.configList = new ArrayList<>();
        this.configList.add(config);
    }

    public void addFTPConfig(Boolean enable) {
        if(this.getFtpConfig() == null) {

            //create
            SourceConfig sourceConfig = new SourceConfig();
            String user = StringUtil.generateLetters(14);
            String password = StringUtil.generateLetters(14);
            sourceConfig.createFTP(user, password, this);
            this.configList.add(sourceConfig);

        }
            Optional.ofNullable(this.getFtpConfig())
                    .ifPresent(x -> x.setEnable(enable));
    }

    public boolean isOwner(String userId) {
        return this.userId != null &&
                this.userId.equals(userId);
    }

    public String[] getDataRow() {
        return new String[]{
                String.valueOf(this.name),
                this.ip,
                String.valueOf(this.port),
                String.valueOf(this.stationId),
                String.valueOf(this.latitude),
                String.valueOf(this.longitude)
        };
    }
}
