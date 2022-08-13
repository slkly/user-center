package com.irene.usercenter.service;

import com.irene.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 * @author irene
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request http请求
     * @return 返回受影响行数
     */
    int userLogout(HttpServletRequest request);

    /**
     * 用户脱敏函数
     * @param originUser 脱敏前的user对象
     * @return 脱敏后的safetyUser对象
     */
    User getSafetyUser(User originUser);

}
