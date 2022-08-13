package com.irene.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author irene
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -568879352023682029L;

    /**
     * 状态码
     */
    private int code;

    /**
     * （成功时）返回的数据，泛型提高可复用性
     */
    private T data;

    /**
     * 状态码信息
     */
    private String message;

    /**
     * 状态码描述
     */
    private String description;


    public BaseResponse() {
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

}
