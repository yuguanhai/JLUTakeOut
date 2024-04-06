package com.jlu.takeout.service;

import com.jlu.takeout.dto.UserLoginDTO;
import com.jlu.takeout.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * 微信登录
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
