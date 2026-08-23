package com.cheems.blog.utils;

import com.cheems.blog.common.exception.ErrorCode;
import com.cheems.blog.common.result.Result;

public class ResultUtils {

    public static <T> Result<T> success(T data) {
        return  new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }


}
