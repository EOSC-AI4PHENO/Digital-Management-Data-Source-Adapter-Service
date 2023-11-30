package com.siseth.adapter.component.date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DatePartReader {

    private Integer from;
    private Integer to;

    public Integer getValue(String name) {
        return Integer.valueOf( name.substring(from, to + 1));
    }

    public DatePartReader(String str) {
        this.from = Integer.valueOf(str.split("-")[0]);
        this.to = Integer.valueOf(str.split("-")[1]);
    }

}
