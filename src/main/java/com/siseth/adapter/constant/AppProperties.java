package com.siseth.adapter.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperties {

    public static String VERSION;

    public static Boolean FILE_SYNCHRONIZE_SCHEDULE;

    public static Boolean CHECK_LAST_SEND_FILE_SCHEDULE;

    public static Boolean CHECK_SPACE_ON_DISC;

    public static Boolean DOWNLOAD_FILE_FROM_DAHUA_SCHEDULE;

    public static Boolean CHECK_TIMEZONE_SCHEDULE;

    public static String SSH_USER;
    public static String SSH_PASSWORD;
    public static String SSH_HOST;
    public static Integer SSH_PORT;

    public static String FTP_HOST;

    public static Integer FTP_PORT;

    @Value("${app.version}")
    public void setVersion(String version) {
        VERSION = version;
    }
    @Value("${app.schedule.file-synchronize}")
    public void setFileSchedule(Boolean schedule) {
        FILE_SYNCHRONIZE_SCHEDULE = schedule;
    }

    @Value("${app.schedule.check-last-send-file}")
    public void setCheckLastSendFile(Boolean schedule) {
        CHECK_LAST_SEND_FILE_SCHEDULE = schedule;
    }

    @Value("${app.schedule.check-space-on-disc}")
    public void setCheckSpaceOnDisc(Boolean schedule) {
        CHECK_SPACE_ON_DISC = schedule;
    }

    @Value("${app.schedule.download-file-from-dahua}")
    public void setDownloadFileFromDahua(Boolean schedule) {
        DOWNLOAD_FILE_FROM_DAHUA_SCHEDULE = schedule;
    }

    @Value("${app.schedule.check-timezone}")
    public void setCheckTimezoneSchedule(Boolean schedule) {
        CHECK_TIMEZONE_SCHEDULE = schedule;
    }




    @Value("${app.ssh.username}")
    public void setSSHUsername(String username) {
        SSH_USER = username;
    }

    @Value("${app.ssh.password}")
    public void setSSHPassword(String password) {
        SSH_PASSWORD = password;
    }

    @Value("${app.ssh.host}")
    public void setSSHHost(String host) {
        SSH_HOST= host;
    }

    @Value("${app.ssh.port}")
    public void setSSHPort(Integer port) {
        SSH_PORT = port;
    }

    @Value("${app.ftp.host}")
    public void setFTPHost(String host) {
        FTP_HOST= host;
    }

    @Value("${app.ftp.port}")
    public void setFTPPort(Integer port) {
        FTP_PORT = port;
    }
}
