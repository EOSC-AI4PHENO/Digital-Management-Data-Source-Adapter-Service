package com.siseth.adapter.statistics.service;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.feign.fedora.FedoraService;
import com.siseth.adapter.feign.fedora.dto.FilesSizeDTO;
import com.siseth.adapter.repository.source.ImageSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {

    private final ImageSourceRepository imageSourceRepository;
    private final FedoraService fedoraService;


    public List<FilesSizeDTO> getStatistics(String userId, String realm) {
        List<ImageSource> imageSources = imageSourceRepository.findAllByIsActiveIsTrueAndUserIdAndRealm(userId, realm);

        return  imageSources.stream().map(source->fedoraService.getSizeByOwner(userId, realm, source.getId())
                        .setParams(source))
                        .collect(Collectors.toList());
    }
}
