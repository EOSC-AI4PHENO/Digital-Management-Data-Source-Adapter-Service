package com.siseth.adapter.entity.source;

import com.siseth.adapter.component.entity.BaseEntity;
import com.siseth.adapter.constant.AppProperties;
import com.siseth.adapter.ssh.CreateNewFTPUser;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "source", name = "`T_SOURCE_CONFIGURATION`")
@Where(clause = "`isActive`")
public class SourceConfig extends BaseEntity {

    @Column(name="`desc`")
    private String desc;

    @Column(name="`ip`")
    private String ip;

    @Column
    private Integer port;

    @Column(name="`user`")
    private String user;

    @Column(name="`password`")
    private String password;

    @Column(name="`enable`")
    private Boolean enable;

    @Column(name="`obj`")
    private String obj;

    @Enumerated(EnumType.STRING)
    @Column(name="`type`")
    private ConfigType type;

    @Column(name="`isActive`")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "`sourceId`")
    private ImageSource source;

    public SourceConfig create(String days, ImageSource source) {
        this.isActive = true;
        this.enable = true;
        this.type = ConfigType.DAYS;
        this.obj = days;
        this.source = source;
        return this;
    }

    public void createFTP(String user, String password, ImageSource source) {
        CreateNewFTPUser ftpUser = new CreateNewFTPUser().createUser(user, password);
        this.ip = AppProperties.FTP_HOST;
        this.port = AppProperties.FTP_PORT;
        this.user = user;
        this.password = password;
        this.type = ConfigType.FTP;
        this.isActive = true;
        this.source = source;
    }

    public List<String> getDays() {
        if(!this.type.equals(ConfigType.DAYS) || this.obj == null)
            return new ArrayList<>();
        return List.of(this.obj.split(","));
    }

    public enum ConfigType {
                FTP,
                DAYS
    }

}
