package com.siseth.adapter.component.dahua;

import com.siseth.adapter.entity.source.ImageSource;

import java.util.Map;
import java.util.Optional;

import static com.siseth.adapter.entity.source.ImageSource.SourceType.getDahuaType;

public final class DahuaConfig {

    public static final Map<ImageSource.SourceType, CameraDefaultData> camera2deviceType
            = Map.of(
            ImageSource.SourceType.DAHUA_5,
                new CameraDefaultData("IPC-HFW2531S-S-S2",
                                    "MAIN/DATE/CHANNEL/IMAGE_TYPE/HOUR",
                                    "minute:0-1,second:3-4"),
            ImageSource.SourceType.DAHUA_12,
                new CameraDefaultData("IPC-HFW71242H-Z",
                                    "MAIN/DEVICE_TYPE/DATE",
                                    "year:4-7,month:8-9,day:10-11,hour:12-13,minute:14-15"
                                         ),
            ImageSource.SourceType.DAHUA_8,
                new CameraDefaultData("IPC-HFW3841T-ZAS-27135-S2",
                        "MAIN/DEVICE_TYPE/DATE",
                        "year:4-7,month:8-9,day:10-11,hour:12-13,minute:14-15"
                                    )
    );



    public static void checkType(ImageSource.SourceType type, String deviceType) {
        if(!getDahuaType().contains(type))
            return;
        if(camera2deviceType.get(type) == null || !camera2deviceType.get(type).getDeviceType().equals(deviceType))
            throw new RuntimeException("Wrong device type");
    }

    public static Optional<CameraDefaultData> getConfig(ImageSource.SourceType type) {
        return Optional.ofNullable(camera2deviceType.get(type));
    }

    public static String getDirectoryRegex(ImageSource.SourceType type) {
        if(!getDahuaType().contains(type))
            return null;
        return DahuaConfig.getConfig(type)
                                .map(CameraDefaultData::getDirectoryRegex)
                                .orElse(null);
    }

    public static String getDateNameRegex(ImageSource.SourceType type) {
        if(!getDahuaType().contains(type))
            return null;
        return DahuaConfig.getConfig(type)
                .map(CameraDefaultData::getDateNameRegex)
                .orElse(null);
    }

}
