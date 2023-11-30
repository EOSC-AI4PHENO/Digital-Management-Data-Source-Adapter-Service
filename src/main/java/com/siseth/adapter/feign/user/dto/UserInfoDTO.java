package com.siseth.adapter.feign.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public String getUserBasic() {
        return this.firstName != null && this.lastName != null ?
                this.firstName + " " + this.lastName + " (" + this.email + ")" :
                this.email;
    }

}
