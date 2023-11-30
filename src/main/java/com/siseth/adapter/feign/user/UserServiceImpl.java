package com.siseth.adapter.feign.user;

import com.siseth.adapter.feign.user.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserService userService;


    public UserInfoDTO getUserInfo(String id, String realm) {
        try {
            return userService.getUserInfo(id, realm);
        } catch (Exception e) {
            log.error("Error when try connect to User-Service, message {}", e.getMessage());
            return null;
        }
    }

}
