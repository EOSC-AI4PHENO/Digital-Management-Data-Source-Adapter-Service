package com.siseth.adapter.feign.fedora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Getter
public class PaginationResDTO {
    private Integer currentPage;
    private Integer pageSize;
    private Long totalElements;
    private Boolean hasPrevious;
    private Boolean hasNext;
    private String sort;

    public PaginationResDTO(Page page){
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.sort = page.getSort().toString();
    }
}
