package com.siseth.adapter.feign.mail;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "mail-service")
public interface MailService {

    @PostMapping("/api/internal/correspondence/mail/createEmail")
    public String send(@RequestBody Map<String, Object> map);



}
