package com.cheems.blog.controller;

import com.cheems.blog.common.result.Result;
import com.cheems.blog.facade.BlogThumbFacade;
import com.cheems.blog.utils.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/thumb")
@RestController
public class ThumbController {

    private final BlogThumbFacade blogThumbFacade;

    public Result<Boolean> doThumb(Long blogId, HttpServletRequest request) {
        return ResultUtils.success(blogThumbFacade.doThumb(blogId, request));
    }

    public Result<Boolean> unThumb(Long blogId, HttpServletRequest request) {
        return ResultUtils.success(blogThumbFacade.unThumb(blogId,request));
    }

}
