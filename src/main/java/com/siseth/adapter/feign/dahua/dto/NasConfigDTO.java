package com.siseth.adapter.feign.dahua.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NasConfigDTO {

    private List<NasConfigItemDTO> items;

    public Boolean isFTPEnable() {
        return getFTP()
                .filter( x -> x.getEnable() != null)
                .map( x -> Boolean.parseBoolean(x.getEnable()))
                .orElse(null);
    }

    public Optional<NasConfigItemDTO> getFTP() {
        return this.items != null ?
                this.items.stream()
                        .filter(x -> x.getProtocol() != null && x.getProtocol().equals("FTP"))
                        .findFirst():
                Optional.empty();
    }


}
