package com.cheems.blog.facade;


import com.cheems.blog.common.exception.BizException;
import com.cheems.blog.common.exception.ErrorCode;
import com.cheems.blog.entity.Blog;
import com.cheems.blog.entity.Thumb;
import com.cheems.blog.entity.User;
import com.cheems.blog.entity.vo.BlogVO;
import com.cheems.blog.service.BlogService;
import com.cheems.blog.service.ThumbService;
import com.cheems.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BlogThumbFacade {

    private final BlogService blogService;
    private final ThumbService thumbService;
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;

    public List<BlogVO> getBlogVOList(HttpServletRequest request) {
        //1.查询所有的blog
        List<Blog> blogList = blogService.list();
        if (blogList.isEmpty()) {
            return List.of();
        }

        List<Long> blogIdList = blogList.stream().map(Blog::getId).toList();
        //2. 查询 用户是否点赞 -> map
        User loginUser = userService.getLoginUser(request);
        Map<Long, Boolean> hasThumbBlogMap = thumbService.isDoThumbMap(blogIdList, loginUser);

        //3. 组装
        List<BlogVO> blogVOList = new ArrayList<>();
        blogList.forEach(blog -> {
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            blogVO.setHasThumb(hasThumbBlogMap.getOrDefault(blog.getId(), false));
            blogVOList.add(blogVO);
        });

        return blogVOList;
    }


    public BlogVO getBlogVOById(long blogId, HttpServletRequest request) {

        //查询文章基本信息
        Blog blog = blogService.getBlogById(blogId);
        if (blog == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        //查询用户是否点赞该文章 如果有记录就是点赞过
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BizException(ErrorCode.UNAUTHORIZED);
        }
        Boolean doThumb = thumbService.isDoThumb(blogId, loginUser);
        // 组装
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        blogVO.setHasThumb(doThumb);

        return blogVO;
    }

    public Boolean doThumb(Long blogId, HttpServletRequest request) {

        //1. 判断blogId 是否存在
        if (blogId == null || blogService.getBlogById(blogId) == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }

        //2. 加锁事务：
        User loginUser = userService.getLoginUser(request);
        //todo 手动事务 + 加锁
        synchronized (loginUser.getId().toString().intern()) {
            return transactionTemplate.execute(status -> {
                //2.1 查询是否已点赞
                Boolean doThumb = thumbService.isDoThumb(blogId, loginUser);
                if (doThumb) {
                    throw new BizException(ErrorCode.PARAM_ERROR, "用户已点赞");
                }
                //2.2 更新blog 表 总数
                boolean blogUpdateResult = blogService.lambdaUpdate().eq(Blog::getId, blogId).setSql("thumb_count = thumb_count + 1").update();
                if (blogUpdateResult) {
                    //2.3 插入thumb 表
                    Thumb thumb = new Thumb();
                    thumb.setUserId(loginUser.getId());
                    thumb.setBlogId(blogId);
                    boolean save = thumbService.save(thumb);
                    if (!save) {
                        throw new BizException(ErrorCode.SYSTEM_ERROR);
                    }
                } else {
                    throw new BizException(ErrorCode.SYSTEM_ERROR);
                }
                return Boolean.TRUE;
            });
        }
    }

    public Boolean unThumb(Long blogId, HttpServletRequest request) {
        //1. 判断blogId是否存在
        if (blogId == null || blogService.getBlogById(blogId) == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }

        //2, 事务 + 加锁
        User loginUser = userService.getLoginUser(request);
        synchronized (loginUser.getId().toString().intern()) {
            return transactionTemplate.execute(status -> {
                //2.1 判断是否点赞
                Boolean doThumb = thumbService.isDoThumb(blogId, loginUser);
                if (!doThumb) {
                    throw new BizException(ErrorCode.PARAM_ERROR, "用户未点赞");
                }
                //2.2 更新blog 点赞总数
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .gt(Blog::getThumbCount, 0)
                        .setSql("thumb_count = thumb_count - 1")
                        .update();
                if (update) {
                    //2.3 删除点赞记录
                    boolean removed = thumbService.lambdaUpdate()
                            .eq(Thumb::getBlogId, blogId)
                            .eq(Thumb::getUserId, loginUser.getId())
                            .remove();
                    if (!removed) {
                        throw new BizException(ErrorCode.SYSTEM_ERROR);
                    }
                } else {
                    throw new BizException(ErrorCode.SYSTEM_ERROR);
                }
                return Boolean.TRUE;
            });
        }
    }
}
