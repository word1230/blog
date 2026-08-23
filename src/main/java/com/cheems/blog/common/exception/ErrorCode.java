package com.cheems.blog.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举：通用段 10000~19999；模块段占位——用户 11xxx、文章 12xxx、评论 13xxx
 *
 * @author cheems
 * @date 2026/08/23
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(200, "成功"),

    PARAM_ERROR(10400, "参数错误"),
    UNAUTHORIZED(10401, "未认证"),
    FORBIDDEN(10403, "无权限"),
    NOT_FOUND(10404, "资源不存在"),
    SYSTEM_ERROR(10500, "系统内部异常"),
    SERVICE_UNAVAILABLE(10503, "服务暂不可用"),





    ;

    private final int code;

    private final String message;
}
