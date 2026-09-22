package com.cheems.blog.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheems.blog.entity.Thumb;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author cheems
* @description 针对表【thumb】的数据库操作Service
* @createDate 2026-08-23 18:36:41
*/
public interface ThumbService extends IService<Thumb> {

    Boolean doThumb(Long blogId, HttpServletRequest request);
}
