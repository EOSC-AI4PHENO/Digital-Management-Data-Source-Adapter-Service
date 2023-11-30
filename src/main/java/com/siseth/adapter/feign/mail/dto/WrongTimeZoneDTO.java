package com.siseth.adapter.feign.mail.dto;

import com.siseth.adapter.entity.source.ImageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrongTimeZoneDTO {

    private Map<String, Object> map;

    public WrongTimeZoneDTO(String mail, String type, ImageSource source, LocalDateTime date){
        this.map = new HashMap<>();
        this.map.put("email", mail);
        this.map.put("type", type);
        this.map.put("realm", source.getRealm());
        if(date != null)
            this.map.put("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.map.put("name", source.getName());
        this.map.put("desc", getDesc(source, type));
        this.map.put("url", source.getIp());
        this.map.put("subject",getSubject(type));
    }


    private String getDesc(ImageSource source, String type) {
        switch (type){
           case "INCORRECT_TIMEZONE":
               return "The camera \"" + source.getName() + "\" is configured with an incorrect timezone. Please review the settings.";
            case "MISSING_FILE":
                return "According to the configuration of the camera \"" + source.getName() + "\" at least one file is missing. " +
                        "Please ensure that all necessary files have been loaded correctly or/and check the connection and status of your camera. \n" +
                        "Date of last entry is: " + this.map.get("date");
            case "LOW_SPACE_DISC":
                return "The camera \"" + source.getName() + "\" is experiencing low disk space. Please free up some space to ensure optimal performance.";
        }
        return "";
    }

    private String getSubject(String type) {
        switch (type){
            case "INCORRECT_TIMEZONE":
                return "Wrong timezone";
            case "MISSING_FILE":
                return "Missing file from camera";
            case "LOW_SPACE_DISC":
                return "Low space in camera memory";
        }
        return "";
    }
}

