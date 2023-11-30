package com.siseth.adapter.feign.dahua.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NasConfigItemDTO{

    private String protocol;

    private String address;

    private String directory;

    private String enable;

    private String port;

    private String user;

}
