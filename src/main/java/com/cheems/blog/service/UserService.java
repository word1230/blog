package com.cheems.blog.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheems.blog.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;


/**
* @author cheems
* @description 针对表【user】的数据库操作Service
* @createDate 2026-08-23 18:15:29
*/
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param userId
     * @param request
     * @return
     */
    User login(Integer userId, HttpServletRequest request);


    /**
     * 获取登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

}
