package com.siseth.adapter.component.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DateFromNameFormat {

    private String fullPath;
    private String name;

    private String directoryRegex;

    private String dateNameRegex;

    private DateCreator dateCreator;

    public DateFromNameFormat(String name, String dateNameRegex) {
        this.name = name;
        this.dateNameRegex = dateNameRegex;
        this.dateCreator = new DateCreator()
                                    .createFromNameRegex(this.name, this.dateNameRegex);
    }


    public DateFromNameFormat(String path, String name, String directoryRegex, String dateNameRegex) {
        this.fullPath = path;
        this.name = name;
        this.directoryRegex = directoryRegex;
        this.dateNameRegex = dateNameRegex;
        this.dateCreator = new DateCreator()
                                    .createFromDirectoryRegex(this.fullPath, this.directoryRegex)
                                    .createFromNameRegex(this.name, this.dateNameRegex);

    }

    public LocalDateTime getDateTime() {
        return this.dateCreator.create();
    }




}
