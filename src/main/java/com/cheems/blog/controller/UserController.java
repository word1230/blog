package com.cheems.blog.controller;

import com.cheems.blog.common.result.Result;
import com.cheems.blog.entity.User;
import com.cheems.blog.service.UserService;
import com.cheems.blog.utils.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping
    public Result<User> login(Integer userId, HttpServletRequest request) {
        return ResultUtils.success(userService.login(userId, request));
    }


}


