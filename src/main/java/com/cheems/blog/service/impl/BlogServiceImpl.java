package com.cheems.blog.service.impl;


import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheems.blog.entity.Blog;
import com.cheems.blog.service.BlogService;
import com.cheems.blog.mapper.BlogMapper;
import org.springframework.stereotype.Service;

/**
* @author cheems
* @description 针对表【blog】的数据库操作Service实现
* @createDate 2026-08-23 18:19:14
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService{

}




