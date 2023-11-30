package com.siseth.adapter.feign.weatherStation.dto.source.response;

import com.siseth.adapter.component.paggingSorting.PaginationResDTO;
import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceWithPageResDTO {

    private PaginationResDTO page;

    private List<ImageSourceShortResDTO> imageSources;

    public ImageSourceWithPageResDTO(Page<ImageSource> page, String userId) {
        this.page = new PaginationResDTO(page);
        this.imageSources =  page.get()
                                    .map(x -> new ImageSourceShortResDTO(x, userId))
                                    .collect(Collectors.toList());
    }

}
