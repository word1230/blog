package com.cheems.blog.controller;

import com.cheems.blog.common.result.Result;
import com.cheems.blog.entity.vo.BlogVO;
import com.cheems.blog.service.BlogService;
import com.cheems.blog.utils.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/blog")
@RestController
public class BlogController {

    private final BlogService blogService;


    @GetMapping("/get")
    public Result<BlogVO> getBlogVOById(long blogId, HttpServletRequest request) {
        return ResultUtils.success(blogService.getBlogVOById(blogId, request));
    }


}
