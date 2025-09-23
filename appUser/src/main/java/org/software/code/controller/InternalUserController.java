package org.software.code.controller;

import org.software.code.entity.User;
import org.software.code.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务内部API控制器
 * 用于处理来自其他服务的Feign调用
 */
@RestController
@RequestMapping("/user")
public class InternalUserController {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 验证用户支付密码
     */
    @PostMapping("/{userId}/verify-pay-password")
    public Boolean verifyPayPassword(@PathVariable("userId") Long userId,
                                   @RequestParam("payPassword") String payPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(payPassword, user.getPayPassword());
    }

    /**
     * 获取用户基本信息
     */
    @GetMapping("/{userId}/basic")
    public User getUserBasicInfo(@PathVariable("userId") Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 获取用户昵称
     */
    @GetMapping("/{userId}/nickname")
    public String getUserNickname(@PathVariable("userId") Long userId) {
        User user = userMapper.selectById(userId);
        return user != null ? user.getNickname() : null;
    }
} 