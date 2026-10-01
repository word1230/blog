package com.cheems.blog.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheems.blog.common.exception.BizException;
import com.cheems.blog.common.exception.ErrorCode;
import com.cheems.blog.entity.Blog;
import com.cheems.blog.entity.Thumb;
import com.cheems.blog.entity.User;
import com.cheems.blog.entity.vo.BlogVO;
import com.cheems.blog.mapper.BlogMapper;
import com.cheems.blog.service.BlogService;
import com.cheems.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cheems
 * @description 针对表【blog】的数据库操作Service实现
 * @createDate 2026-08-23 18:19:14
 */
@RequiredArgsConstructor
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {


    private UserService userService;

    @Override
    public Blog getBlogById(long blogId) {
        if (StrUtil.isBlankIfStr(blogId)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "blogId不能为空");
        }
        Blog blog = this.getById(blogId);
        return blog;
    }

}




