package com.cheems.blog.controller;

import com.cheems.blog.common.result.Result;
import com.cheems.blog.service.ThumbService;
import com.cheems.blog.utils.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/thumb")
@RestController
public class ThumbController {

    private final ThumbService thumbService;

    public Result<Boolean> doThumb( Long blogId, HttpServletRequest request) {
        return ResultUtils.success(thumbService.doThumb(blogId,request));
    }


}
