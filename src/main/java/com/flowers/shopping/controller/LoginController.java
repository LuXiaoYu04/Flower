package com.flowers.shopping.controller;

import com.flowers.shopping.entity.LoginInfo;
import com.flowers.shopping.entity.Result;
import com.flowers.shopping.entity.User;
import com.flowers.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录模块
 */
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     */
    @RequestMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("登录：{}",user);
        LoginInfo loginInfo = userService.login(user);
        if (loginInfo != null){
            return Result.success(loginInfo);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}
