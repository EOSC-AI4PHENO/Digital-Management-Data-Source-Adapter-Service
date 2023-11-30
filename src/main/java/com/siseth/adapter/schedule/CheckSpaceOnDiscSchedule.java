package com.siseth.adapter.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.dahua.DahuaServiceImpl;
import com.siseth.adapter.feign.dahua.dto.DeviceInfoItemDTO;
import com.siseth.adapter.feign.dahua.dto.DeviceInfoItemDetailDTO;
import com.siseth.adapter.feign.mail.MailService;
import com.siseth.adapter.feign.mail.dto.WrongTimeZoneDTO;
import com.siseth.adapter.feign.user.UserService;
import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import com.siseth.adapter.repository.source.ImageSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.siseth.adapter.constant.AppProperties.CHECK_SPACE_ON_DISC;
import static com.siseth.adapter.constant.AppProperties.CHECK_TIMEZONE_SCHEDULE;
import static com.siseth.adapter.entity.source.ImageSource.SourceType.getDahuaType;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckSpaceOnDiscSchedule {

    private final BigDecimal limit = BigDecimal.valueOf(0.9);

    private final ImageSourceRepository repository;

    private final UserService userService;

    private final MailService mailService;

    private final DahuaServiceImpl dahuaService;


    @Scheduled(cron = "0 0 * * * *")
    @SneakyThrows
    public void schedule() {
        log.debug("Start Space On disc: " + CHECK_SPACE_ON_DISC);
        if(!CHECK_SPACE_ON_DISC)
            return;

        List<ImageSource> sources = repository.findAllByTypeInAndIsActiveIsTrue(getDahuaType());

        for (ImageSource source : sources) {

            List<DeviceInfoItemDTO> itemList = dahuaService.getDeviceAllInfo(source.getIp(), source.getPort(),
                                                                              source.getCameraUser(), source.getCameraPassword());

            for (DeviceInfoItemDTO deviceInfoItemDTO : itemList) {
                BigDecimal total =
                                deviceInfoItemDTO.getDetails() != null ?
                                        deviceInfoItemDTO.getDetails()
                                                .stream()
                                                .findFirst()
                                                .map(DeviceInfoItemDetailDTO::getTotalBytes)
                                                .map(BigDecimal::new)
                                                .orElse(null) :
                                        null;
                BigDecimal used =
                        deviceInfoItemDTO.getDetails() != null ?
                                deviceInfoItemDTO.getDetails()
                                        .stream()
                                        .findFirst()
                                        .map(DeviceInfoItemDetailDTO::getUsedBytes)
                                        .map(BigDecimal::new)
                                        .orElse(null) :
                                null;

                if(total != null && used != null &&
                        used.divide(total,2, RoundingMode.CEILING).compareTo(this.limit) > -1) {
                    UserInfoDTO user = userService.getUserInfo(source.getUserId(), source.getRealm());
                    WrongTimeZoneDTO dto = new WrongTimeZoneDTO(user.getEmail(), "LOW_SPACE_DISC",  source, null);
                    mailService.send(dto.getMap());
                }

            }
        }



    }

}
