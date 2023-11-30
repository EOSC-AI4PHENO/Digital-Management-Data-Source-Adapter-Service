package com.siseth.adapter.feign.dahua.dto;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.entity.source.SourceConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SetNasConfigDTO {

    private String address;

    private String directory;

    private String enable;

    private String port;

    private String password;

    private String user;

    public SetNasConfigDTO(ImageSource source) {
        this(source, true, false);
    }

    public SetNasConfigDTO(ImageSource source, Boolean enable, Boolean configExist) {
        SourceConfig config = source.getFtpConfig();
        this.address = configExist ? null : config.getIp();
        this.directory = configExist ? null : source.getDirectory();
        this.enable = enable ?  "true" : "false";
        this.port = configExist ? null :  config.getPort().toString();
        this.password = configExist ? null :  config.getPassword();
        this.user = configExist ? null : config.getUser();
    }

}
