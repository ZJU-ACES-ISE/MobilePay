package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.software.code.common.result.Result;
import org.software.code.dto.PasswordUpdateRequest;
import org.software.code.dto.UserProfileUpdateRequest;
import org.software.code.entity.User;
import org.software.code.mapper.UserMapper;
import org.software.code.service.UserService;
import org.software.code.service.VerifyCodeService;
import org.software.code.common.util.JwtUtil;
import org.software.code.common.util.RedisUtil;
import org.software.code.vo.UserRegisterVo;
import org.software.code.vo.UserVo;
import org.software.code.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import io.jsonwebtoken.Claims;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private VerifyCodeService verifyCodeService;
    
    @Autowired
    private RedisUtil redisUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // JWT Token过期时间，30天
    private static final long TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;
    
    // Redis中黑名单token的前缀
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    
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
    
    @Override
    public Result<UserLoginVo> login(String phone, String loginType, String credential) {
        // 查询用户是否存在
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
        );
        
        if (user == null) {
            return Result.failed("用户不存在", UserLoginVo.class);
        }
        
        // 根据登录类型验证凭证
        boolean isAuthenticated = false;
        
        if ("password".equals(loginType)) {
            // 密码登录
            if (!passwordEncoder.matches(credential, user.getLoginPassword())) {
                return Result.failed("密码不正确", UserLoginVo.class);
            }
            isAuthenticated = true;
        } else if ("verifyCode".equals(loginType)) {
            // 验证码登录 - 调用验证码服务进行验证
            Result<?> checkResult = verifyCodeService.checkVerifyCode(phone, credential, "login");
            if (checkResult.getCode() != 200) {
                return Result.failed(checkResult.getMessage(), UserLoginVo.class);
            }
            isAuthenticated = true;
        } else {
            return Result.failed("不支持的登录类型", UserLoginVo.class);
        }
        
        if (!isAuthenticated) {
            return Result.failed("认证失败", UserLoginVo.class);
        }
        
        // 验证通过，生成token
        String token = JwtUtil.generateJWToken(user.getId(), TOKEN_EXPIRE_TIME);
        
        // 构建返回对象
        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setUserId(String.valueOf(user.getId()));
        loginVo.setToken(token);
        loginVo.setPhone(phone);
        
        return Result.success("登录成功", loginVo);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserVo> updateUserProfile(String token, UserProfileUpdateRequest request) {
        try {
            // 从token中提取用户ID
            long userId = JwtUtil.extractID(token);
            
            // 查询用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.failed("用户不存在", UserVo.class);
            }
            
            boolean needUpdate = false;
            
            // 更新昵称
            if (StringUtils.hasText(request.getNickName())) {
                user.setNickname(request.getNickName());
                needUpdate = true;
            }
            
            // 更新手机号（需要验证手机号是否已被使用）
            if (StringUtils.hasText(request.getPhone()) && !request.getPhone().equals(user.getPhone())) {
                // 检查手机号是否已被其他用户使用
                User existUser = userMapper.selectOne(
                    new LambdaQueryWrapper<User>()
                        .eq(User::getPhone, request.getPhone())
                        .ne(User::getId, userId)
                );
                
                if (existUser != null) {
                    return Result.failed("手机号已被其他用户使用", UserVo.class);
                }
                
                user.setPhone(request.getPhone());
                needUpdate = true;
            }
            
            // 更新头像
            if (StringUtils.hasText(request.getAvatarUrl())) {
                user.setAvatarUrl(request.getAvatarUrl());
                needUpdate = true;
            }
            
            // 只有在有字段更新时才进行数据库操作
            if (needUpdate) {
                user.setUpdateTime(LocalDateTime.now());
                userMapper.updateById(user);
            }
            
            // 无论是否更新，都返回最新的用户信息
            UserVo userVo = UserVo.builder()
                    .userId(String.valueOf(user.getId()))
                    .nickName(user.getNickname())
                    .phone(user.getPhone())
                    .avatarUrl(user.getAvatarUrl())
                    .build();
            
            return Result.success(needUpdate ? "修改成功" : "未做任何修改", userVo);
        } catch (Exception e) {
            return Result.failed("更新失败：" + e.getMessage(), UserVo.class);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updatePaymentPassword(String token, PasswordUpdateRequest request) {
        try {
            // 从token中提取用户ID
            long userId = JwtUtil.extractID(token);
            
            // 查询用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.failed("用户不存在");
            }
            
            // 验证旧密码是否正确
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPayPassword())) {
                // 返回401状态码，表示原密码不正确
                Result<?> result = Result.failed("原密码不正确");
                result.setCode(401); // 设置为401状态码
                return result;
            }
            
            // 加密并更新新的支付密码
            user.setPayPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setUpdateTime(LocalDateTime.now());
            
            // 更新数据库
            userMapper.updateById(user);
            
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.failed("修改支付密码失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updatePassword(String token, PasswordUpdateRequest request) {
        try {
            // 从token中提取用户ID
            long userId = JwtUtil.extractID(token);
            
            // 查询用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.failed("用户不存在");
            }
            
            // 验证旧密码是否正确
            if (!passwordEncoder.matches(request.getOldPassword(), user.getLoginPassword())) {
                // 返回401状态码，表示原密码不正确
                Result<?> result = Result.failed("原密码不正确");
                result.setCode(401); // 设置为401状态码
                return result;
            }
            
            // 加密并更新新的登录密码
            user.setLoginPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setUpdateTime(LocalDateTime.now());
            
            // 更新数据库
            userMapper.updateById(user);
            
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.failed("修改登录密码失败：" + e.getMessage());
        }
    }

    @Override
    public Result<?> logout(String token) {
        try {
            // 从token中提取用户ID，确保token有效
            long userId = JwtUtil.extractID(token);
            
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 将token加入Redis黑名单
            // 获取token的过期时间（作为黑名单中的过期时间）
            Claims claims = JwtUtil.extractAllClaims(token);
            Date expiration = claims.getExpiration();
            long ttl = Math.max(0, expiration.getTime() - System.currentTimeMillis());
            
            // 将token的唯一标识添加到黑名单
            String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
            redisUtil.setValue(blacklistKey, String.valueOf(userId), ttl);
            
            return Result.success("退出成功");
        } catch (Exception e) {
            return Result.failed("无效的登录状态");
        }
    }
} 