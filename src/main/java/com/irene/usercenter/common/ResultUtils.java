package com.irene.usercenter.common;

/**
 * 返回工具类
 *
 * @author irene
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败 - 只定义描述，不更改消息（消息从ErrorCode中取）
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

     /**
     * 失败 - 定义消息和描述
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败 - code状态码也能自己定义
     * @param code
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

}
