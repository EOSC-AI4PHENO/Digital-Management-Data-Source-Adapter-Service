package com.siseth.adapter.schedule;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.entity.source.SourceConfig;
import com.siseth.adapter.feign.fedora.FedoraService;
import com.siseth.adapter.feign.mail.MailService;
import com.siseth.adapter.feign.mail.dto.WrongTimeZoneDTO;
import com.siseth.adapter.feign.user.UserService;
import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import com.siseth.adapter.repository.source.ImageSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.siseth.adapter.constant.AppProperties.CHECK_LAST_SEND_FILE_SCHEDULE;
import static com.siseth.adapter.entity.source.ImageSource.SourceType.getDahuaType;


@Component
@RequiredArgsConstructor
@Slf4j
public class CheckLastSendFileSchedule {

    private final ImageSourceRepository repository;

    private final FedoraService fedoraService;

    private final UserService userService;

    private final MailService mailService;

    @Scheduled(cron = "0 30 20 * * *")
//    @Scheduled(initialDelay = 10000, fixedRate = 1000000)
    @Transactional
    public void schedule() {
        log.debug("Start CheckLastSendFileSchedule: " + CHECK_LAST_SEND_FILE_SCHEDULE);
        if(!CHECK_LAST_SEND_FILE_SCHEDULE)
            return;

        String dayNow = LocalDateTime.now().getDayOfWeek().toString();

        //pobierz listę kamer
        List<ImageSource> sources = repository.findAllByTypeInAndIsActiveIsTrue(getDahuaType());

        // dla każdej kamery
        for (ImageSource source : sources) {

            // pobierz konfigurację FTP lub do Fedory
            SourceConfig dayConfig = source.getConfig(SourceConfig.ConfigType.DAYS);
            SourceConfig ftpConfig = source.getConfig(SourceConfig.ConfigType.FTP);
            if(ftpConfig == null || dayConfig == null)
                continue;
            LocalDateTime date = fedoraService.getLastPhotoDateToSourceId(source.getId());

            String dayOfWeek = String.valueOf(Optional.ofNullable(date)
                                        .map(LocalDateTime::getDayOfWeek)
                                        .orElse(null));
            List<String> days = dayConfig.getDays();

            if(ftpConfig.getEnable() &&
                    (dayOfWeek == null || !dayOfWeek.equals(dayNow))  &&
                    days.contains(dayNow)) {
                UserInfoDTO user = userService.getUserInfo(source.getUserId(), source.getRealm());
                WrongTimeZoneDTO dto = new WrongTimeZoneDTO(user.getEmail(), "MISSING_FILE",  source, date);
                mailService.send(dto.getMap());
            }

        }

    }

}
