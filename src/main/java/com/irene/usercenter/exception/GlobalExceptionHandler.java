package com.irene.usercenter.exception;

import com.irene.usercenter.common.BaseResponse;
import com.irene.usercenter.common.ErrorCode;
import com.irene.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author irene
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //通过注解针对什么异常进行什么处理 - 自定义的异常
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    // 默认的异常如何处理
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runTimeExceptionHandler(RuntimeException e) {
        // 记录日志
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
