package com.jlu.takeout.controller.user;

import com.jlu.takeout.constant.JwtClaimsConstant;
import com.jlu.takeout.properties.JwtProperties;
import com.jlu.takeout.dto.UserLoginDTO;
import com.jlu.takeout.entity.User;
import com.jlu.takeout.result.Result;
import com.jlu.takeout.service.UserService;
import com.jlu.takeout.utils.JwtUtil;
import com.jlu.takeout.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "c端用户接口")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @ApiOperation("微信登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){

        log.info("微信用户登录:{}",userLoginDTO.getCode());
        //微信登录
        User user = userService.wxLogin(userLoginDTO);
        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }
}
