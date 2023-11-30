package com.siseth.adapter.feign.user;

import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-service")
public interface UserService {

    @GetMapping("/api/internal/access/users/getUserInfo")
    UserInfoDTO getUserInfo(@RequestParam String id,
                            @RequestParam String realm);

}
