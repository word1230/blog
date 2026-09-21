package com.cheems.blog.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheems.blog.entity.Blog;
import com.cheems.blog.entity.vo.BlogVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author cheems
* @description 针对表【blog】的数据库操作Service
* @createDate 2026-08-23 18:19:14
*/
public interface BlogService extends IService<Blog> {

    BlogVO getBlogVOById(long blogId, HttpServletRequest request);

    /**
     * 查询blogvo列表
     * @return
     */
    List<BlogVO> getBlogVOList(HttpServletRequest request);

}
