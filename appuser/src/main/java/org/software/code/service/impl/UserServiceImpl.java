package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.software.code.common.result.Result;
import org.software.code.entity.User;
import org.software.code.mapper.UserMapper;
import org.software.code.service.UserService;
import org.software.code.common.util.JwtUtil;
import org.software.code.vo.UserRegisterVo;
import org.software.code.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // JWT Token过期时间，30天
    private static final long TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

    
    @Override
    public Result<UserVo> getUserProfile(String token) {
        try {
            // 从token中提取用户ID
            long userId = JwtUtil.extractID(token);
            
            // 查询用户信息
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.failed("用户不存在", UserVo.class);
            }
            
            // 转换为VO对象
            UserVo userVo = new UserVo();
            userVo.setUserId(String.valueOf(user.getId()));
            userVo.setPhone(user.getPhone());
            userVo.setNickName(user.getNickname() != null ? user.getNickname() : "用户");
            userVo.setAvatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
            
            return Result.success("查询成功", userVo);
        } catch (Exception e) {
            return Result.failed("Token无效或已过期", UserVo.class);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserRegisterVo> register(String phone, String loginPassword, String payPassword) {
        // 再次检查手机号是否已注册
        User existUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
        );
        
        if (existUser != null) {
            return Result.failed("手机号已被注册", UserRegisterVo.class);
        }
        
        // 创建用户
        User user = new User();
        user.setPhone(phone);
        
        // 密码加密
        user.setLoginPassword(passwordEncoder.encode(loginPassword));
        user.setPayPassword(passwordEncoder.encode(payPassword));
        
        // 设置默认值
        user.setNickname("用户" + phone.substring(7)); // 使用手机号后4位作为默认昵称
        user.setAvatarUrl(""); // 默认头像为空
        
        // 设置创建时间
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        // 插入数据库
        userMapper.insert(user);
        
        // 生成token
        String token = JwtUtil.generateJWToken(user.getId(), TOKEN_EXPIRE_TIME);
        
        // 构建返回对象
        UserRegisterVo registerVo = new UserRegisterVo();
        registerVo.setUserId(String.valueOf(user.getId()));
        registerVo.setToken(token);
        registerVo.setPhone(phone);
        
        return Result.success("注册成功", registerVo);
    }
} 