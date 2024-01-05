package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * 微信登录
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
