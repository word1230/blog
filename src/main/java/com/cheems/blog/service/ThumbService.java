package com.cheems.blog.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheems.blog.entity.Thumb;
import com.cheems.blog.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
* @author cheems
* @description 针对表【thumb】的数据库操作Service
* @createDate 2026-08-23 18:36:41
*/
public interface ThumbService extends IService<Thumb> {

    /**
     * 用户对于这个博客是否点赞
     * @param blogId
     * @param loginUser
     * @return
     */
    Boolean isDoThumb(Long blogId, User loginUser);


    /**
     * 用户对于这一批博客所有的点赞情况
     * @return
     */
    Map<Long,Boolean>  isDoThumbMap(List<Long> blogIds, User loginUser);

}
