package com.siseth.adapter.schedule;

import com.siseth.adapter.component.file.FileUtil;
import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.fedora.FedoraService;
import com.siseth.adapter.feign.fedora.dto.FedoraFileExistsDTO;
import com.siseth.adapter.ftp.FTPFileSearcher;
import com.siseth.adapter.ftp.FileOV;
import com.siseth.adapter.repository.source.ImageSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static com.siseth.adapter.constant.AppProperties.FILE_SYNCHRONIZE_SCHEDULE;
import static com.siseth.adapter.entity.source.ImageSource.SourceType.getDahuaType;

@Component
@RequiredArgsConstructor
@Slf4j
public class SynchronizeFiles {

    private final FedoraService fedoraService;

    private final ImageSourceRepository imageSourceRepository;

//    @Scheduled(cron = "0 25 * * * *")
    @Scheduled(initialDelay = 6000L, fixedRate = 3600000L)
    @Transactional
    public void synchronize() throws IOException {
        log.debug("Start synchronize files: " + FILE_SYNCHRONIZE_SCHEDULE);
        if(!FILE_SYNCHRONIZE_SCHEDULE)
            return;

        LocalDate from = LocalDate.now().minusDays(2);
        LocalDate to = LocalDate.now().plusDays(1);

        List<ImageSource> cameras = imageSourceRepository.getAllToSynchronizeWithFTP(getDahuaType());

        //for each camera
        for (ImageSource camera : cameras) {
            FTPFileSearcher searcher =
                    new FTPFileSearcher(camera)
                            .init()
                            .initDate(from, to);

            List<FileOV> files = searcher.getAllFiles()
                    .stream()
                    .filter(x -> !fedoraService.checkImage(x.getDirectory(), camera.getId(), x.getName(), camera.getRealm()))
                    .collect(Collectors.toList());

            files.forEach(x -> x.setFile(searcher.createFile(x.getName(), x.getDirectory())));

            log.debug("Try send to Fedora");
            int added = sendToFedora(camera, files);
            searcher.close();
            if(added > 0) {
                camera.sync();
                LocalDateTime lastPhoto = fedoraService.getLastPhotoDateToSourceId(camera.getId());
                camera.setLastPhotoUploadAt(OffsetDateTime.of(lastPhoto, ZoneOffset.UTC));
                imageSourceRepository.save(camera);
            }
        }

        log.debug("End of schedule");

    }

    private Integer sendToFedora(ImageSource source, List<FileOV> files) {
        int newFiles = 0;
        for (FileOV file : files) {
            MultipartFile multipartFile = FileUtil.convert(file.getFile());
            FedoraFileExistsDTO dto = fedoraService.uploadFileTest(multipartFile,
                                                                    file.getDirectory(),
                                                                    source.getId(),
                                                                    file.getName(),
                                                                    file.getRegisterAt(),
                                                                    source.getRealm(),
                                                                    source.getUserId());
            if(!dto.getIsExist())
                newFiles++;
        }
        return newFiles;
    }

}
