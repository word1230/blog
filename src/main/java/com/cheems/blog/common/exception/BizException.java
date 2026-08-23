package com.cheems.blog.common.exception;

import lombok.Getter;

/**
 * 业务异常：由业务规则主动抛出，携带错误码
 *
 * @author cheems
 * @date 2026/08/23
 */
@Getter
public class BizException extends RuntimeException {

    private final transient ErrorCode errorCode;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
