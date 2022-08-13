package com.irene.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author irene
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 4514301315988292859L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
