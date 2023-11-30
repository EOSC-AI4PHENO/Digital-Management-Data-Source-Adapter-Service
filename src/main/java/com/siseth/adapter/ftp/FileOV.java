package com.siseth.adapter.ftp;

//import com.siseth.adapter.component.date.DateDefaultDahuaFormat;

import com.siseth.adapter.component.date.DateFromNameFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileOV {

    private File file;
    private String name;
    private String directory;
    private LocalDateTime modifiedAt;

    private LocalDateTime registerAt;

    public FileOV(String name, String directory, Calendar modifiedAt, String directoryRegex, String dateNameRegex) {
        this.name = name;
        this.directory = directory;
        this.modifiedAt = LocalDateTime.ofInstant(modifiedAt.toInstant(), ZoneId.systemDefault());
        this.registerAt = new DateFromNameFormat(directory, name, directoryRegex, dateNameRegex).getDateTime();

    }



}
