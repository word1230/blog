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
import com.cheems.blog.service.ThumbService;
import com.cheems.blog.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cheems
 * @description 针对表【blog】的数据库操作Service实现
 * @createDate 2026-08-23 18:19:14
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

    @Resource
    private UserService userService;
    @Resource
    private ThumbService thumbService;

    @Override
    public BlogVO getBlogVOById(long blogId, HttpServletRequest request) {
        if(StrUtil.isBlankIfStr(blogId)){
            throw new BizException(ErrorCode.PARAM_ERROR,"blogId不能为空");
        }
        Blog blog = this.getById(blogId);
        User loginUser = userService.getLoginUser(request);
        return this.getBlogVO(blog, loginUser);
    }

    @Override
    public List<BlogVO> getBlogVOList(HttpServletRequest request) {
        //1. 查找blog
        List<Blog> blogList = this.list();
        if (blogList == null || blogList.size() == 0) {
            return List.of();
        }

        //2. 查找是否点赞
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return List.of();
        }
        QueryWrapper<Thumb> thumbQueryWrapper = new QueryWrapper<>();
        thumbQueryWrapper.eq("user_id", loginUser.getId());
        thumbQueryWrapper.in("blog_id", blogList);
        List<Thumb> thumbList = thumbService.list(thumbQueryWrapper);

        List<Long> thumbBlogId = thumbList.stream().map(Thumb::getBlogId).toList();
        return blogList.stream()
                .map(blog -> {
                    BlogVO blogVO = new BlogVO();
                    BeanUtil.copyProperties(blog, blogVO);
                    blogVO.setHasThumb(thumbBlogId.contains(blog.getId()));
                    return blogVO;
                }).toList();
    }

    private BlogVO getBlogVO(Blog blog, User loginUser) {
        BlogVO blogVO = new BlogVO();
        BeanUtil.copyProperties(blog, blogVO);

        QueryWrapper<Thumb> thumbQueryWrapper = new QueryWrapper<>();
        thumbQueryWrapper.eq("blog_id", blog.getId());
        thumbQueryWrapper.eq("user_id", loginUser.getId());
        Thumb thumb = thumbService.getOne(thumbQueryWrapper);

        blogVO.setHasThumb(thumb != null);
        return blogVO;
    }
}




