package com.siseth.adapter.schedule;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.dahua.DahuaService;
import com.siseth.adapter.feign.dahua.DahuaServiceImpl;
import com.siseth.adapter.feign.mail.MailService;
import com.siseth.adapter.feign.mail.dto.WrongTimeZoneDTO;
import com.siseth.adapter.feign.user.UserService;
import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import com.siseth.adapter.repository.source.ImageSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.siseth.adapter.constant.AppProperties.CHECK_TIMEZONE_SCHEDULE;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckTimezoneSchedule {
    private final ImageSourceRepository repository;
    private final DahuaServiceImpl dahuaService;
    private final MailService mailService;
    private final UserService userService;

    @Scheduled(cron = "0 0 11,14 * * *")
    public void checkTimezone() {
        log.debug("Start CheckTimezoneSchedule: " + CHECK_TIMEZONE_SCHEDULE);
        if(!CHECK_TIMEZONE_SCHEDULE)
            return;

        List<ImageSource> sources = repository.findAllByTypeInAndIsActiveIsTrue(Arrays.asList(ImageSource.SourceType.DAHUA_5, ImageSource.SourceType.DAHUA_12));

        for (ImageSource source : sources) {
            String timezone = dahuaService.getTimezone(source.getIp(),
                                                    source.getPort(),
                                                    source.getCameraUser(),
                                                    source.getCameraPassword());
            if(timezone != null && !timezone.equals("0")){
                log.info("Wrong timezone on camera in ip {}", source.getIp());
                //get mail
                UserInfoDTO user = userService.getUserInfo(source.getUserId(), source.getRealm());
                WrongTimeZoneDTO dto = new WrongTimeZoneDTO(user.getEmail(), "INCORRECT_TIMEZONE",  source, null);
                //send mail
                mailService.send(dto.getMap());
                // change config
                dahuaService.setTimezone(source.getIp(), source.getPort(), source.getCameraUser(), source.getCameraPassword(), "0");
            }
        }

        log.info("End schedule to check timezone on cameras");
    }

}
