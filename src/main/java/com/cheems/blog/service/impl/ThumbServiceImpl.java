package com.cheems.blog.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheems.blog.common.exception.BizException;
import com.cheems.blog.common.exception.ErrorCode;
import com.cheems.blog.entity.Blog;
import com.cheems.blog.entity.Thumb;
import com.cheems.blog.entity.User;
import com.cheems.blog.mapper.BlogMapper;
import com.cheems.blog.mapper.ThumbMapper;
import com.cheems.blog.service.BlogService;
import com.cheems.blog.service.ThumbService;
import com.cheems.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author cheems
 * @description 针对表【thumb】的数据库操作Service实现
 * @createDate 2026-08-23 18:36:41
 */
@RequiredArgsConstructor
@Service
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb>
        implements ThumbService {

    private final UserService userService;
    private final BlogMapper blogMapper;
    private final TransactionTemplate transactionTemplate;


    @Override
    public Boolean doThumb(Long blogId, HttpServletRequest request) {
        if (blogId == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        //1. 校验blogId
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        //2. 获取userid
        User loginUser = userService.getLoginUser(request);
        //3. 加锁，事务插入 todo 加锁+编程式事务
        synchronized (loginUser.getId().toString().intern()) {
            return transactionTemplate.execute(status -> {
                //判断有没有点赞
                boolean exists = this.lambdaQuery()
                        .eq(Thumb::getBlogId, blog.getId())
                        .eq(Thumb::getUserId, loginUser.getId())
                        .exists();
                if (!exists) {
                    throw new BizException(ErrorCode.PARAM_ERROR, "用户已点赞");
                }
                //blog更新总数
                boolean update = this.lambdaUpdate()
                        .eq(Thumb::getBlogId, blog.getId())
                        .setSql("thumb_count = thumb_count + 1")
                        .update();
                //thumb更新明细
                Thumb thumb = new Thumb();
                thumb.setUserId(loginUser.getId());
                thumb.setBlogId(blog.getId());
                //更新成功才执行 todo

                return update && this.save(thumb);
            });
        }

    }
}




