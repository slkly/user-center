package com.irene.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.irene.usercenter.common.BaseResponse;
import com.irene.usercenter.common.ErrorCode;
import com.irene.usercenter.common.ResultUtils;
import com.irene.usercenter.exception.BusinessException;
import com.irene.usercenter.model.domain.User;
import com.irene.usercenter.model.domain.request.UserLoginRequest;
import com.irene.usercenter.model.domain.request.UserRegisterRequest;
import com.irene.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.irene.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.irene.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author irene
 * controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身（越少越好）
 * service 层是对业务逻辑的校验（有可能被 controller 之外的类调用）
 */
// 适用于编写 restful 风格的 api，返回值默认为 json 类型
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 调用自己写的userService.userRegister()
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.NULL_ERROR);
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        return new BaseRespnose<>(0, result, "ok");
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @param request          http请求
     * @return 调用自己写的userService.userLogin()
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request http请求
     * @return 受影响的行数
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户登录态信息
     *
     * @param request http请求
     * @return 调用自己写的userService.getSafetyUser()
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登陆");
        }
        long userId = currentUser.getId();
        // todo 校验用户是否合法
        // 获取登录态对应的用户id后，再根据id去数据库查找一遍，拿到最新的对象数据
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 根据 username 查询用户集合
     *
     * @param username 用户名
     * @return 调用原始的userService.list()
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // 仅管理员可操作
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        // 调用UserService进行用户脱敏方法
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 根据用户id删除用户信息
     *
     * @param id 用户id
     * @return 调用原始的userService.removeById(id)
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 仅管理员可操作
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无操作权限");
        }
        // 删除
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有查询到该用户");
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 是权限校验 - 从登陆状态判断是否为管理员
     * @param request http请求
     * @return 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }

}
