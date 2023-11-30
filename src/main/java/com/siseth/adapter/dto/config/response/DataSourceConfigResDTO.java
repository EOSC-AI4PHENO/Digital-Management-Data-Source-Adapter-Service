package com.siseth.adapter.dto.config.response;

import com.siseth.adapter.entity.source.SourceConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfigResDTO {

    private Long id;

    private String address;

    private Integer port;

    private String user;

    private String password;

    private Boolean enable;

    private String directory;

    public DataSourceConfigResDTO(SourceConfig config) {
        this.id = config.getId();
        this.address = config.getIp();
        this.port = config.getPort();
        this.user = config.getUser();
        this.password = config.getPassword();
        this.enable = config.getEnable();
        this.directory = config.getSource().getDirectory();
    }

}
