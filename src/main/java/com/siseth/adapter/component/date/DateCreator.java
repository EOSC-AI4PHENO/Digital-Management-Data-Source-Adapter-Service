package com.siseth.adapter.component.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DateCreator {

    private Integer year;

    private Integer month;

    private Integer day;

    private LocalDate date;

    private Integer hour;

    private Integer minute;

    private Integer second;

    public DateCreator createFromDirectoryRegex(String path, String regex) {

        if (path == null || regex == null)
            return this;

        List<String> part = Arrays.asList(path.split("/"));
        List<String> regexPath = Arrays.asList(regex.split("/"));

        int i;

        //DATE
        i = regexPath.indexOf("DATE");
        if(i != -1)
            this.date = LocalDate.parse(part.get(i));

        //HOUR
        i = regexPath.indexOf("HOUR");
        if(i != -1)
            this.hour = Integer.parseInt(part.get(i));

        //MINUTES
        i = regexPath.indexOf("MINUTES");
        if(i != -1)
            this.minute = Integer.parseInt(part.get(i));

        //SECOND
        i = regexPath.indexOf("SECOND");
        if(i != -1)
            this.second = Integer.parseInt(part.get(i));

        return this;
    }

    public DateCreator createFromNameRegex(String name, String regex) {
        String[] part = regex.split(",");

        for (String s : part) {
            List<String> pair = Arrays.asList(s.split(":"));

            if(pair.get(0).equals("year"))
                this.year = new DatePartReader(pair.get(1)).getValue(name);

            if(pair.get(0).equals("month"))
                this.month = new DatePartReader(pair.get(1)).getValue(name);

            if(pair.get(0).equals("day"))
                this.day = new DatePartReader(pair.get(1)).getValue(name);

            if(pair.get(0).equals("hour"))
                this.hour = new DatePartReader(pair.get(1)).getValue(name);

            if(pair.get(0).equals("minute"))
                this.minute = new DatePartReader(pair.get(1)).getValue(name);

            if(pair.get(0).equals("second"))
                this.second = new DatePartReader(pair.get(1)).getValue(name);
        }
        return this;
    }

    public LocalDateTime create() {
        if(this.date == null)
            this.date = LocalDate.of(this.year, this.month, this.day);
        return LocalDateTime.of(this.date,
                                    LocalTime.of(this.hour,
                                                    Optional.ofNullable(this.minute).orElse(0),
                                                    Optional.ofNullable(this.second).orElse(0)
                                                    ));
    }

}
