package com.cheems.blog.service.impl;


import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheems.blog.common.constant.UserConstant;
import com.cheems.blog.common.exception.BizException;
import com.cheems.blog.common.exception.ErrorCode;
import com.cheems.blog.entity.User;
import com.cheems.blog.service.UserService;
import com.cheems.blog.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * @author cheems
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2026-08-23 18:15:29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public User login(Integer userId, HttpServletRequest request) {
        if (userId == null) {
            throw new BizException(ErrorCode.PARAM_ERROR, "用户id不能为空");
        }
        User user = this.getById(userId);
        request.getSession().setAttribute(UserConstant.LOGIN_USER, user);
        return user;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.LOGIN_USER);
        if (attribute == null) {
            throw new BizException(ErrorCode.UNAUTHORIZED);
        }
        return (User) attribute;
    }
}




