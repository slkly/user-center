package com.irene.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录信息体
 *
 * @author irene
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 6141487206756008514L;

    private String userAccount;

    private String userPassword;

}
