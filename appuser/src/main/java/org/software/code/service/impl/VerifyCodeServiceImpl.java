package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.software.code.common.result.Result;
import org.software.code.entity.User;
import org.software.code.mapper.UserMapper;
import org.software.code.service.VerifyCodeService;
import org.software.code.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现类
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * Redis中验证码的键前缀
     */
    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    
    /**
     * 验证码有效期（秒）
     */
    private static final int VERIFY_CODE_EXPIRE = 300;
    
    @Override
    public Result<VerifyCodeVo> sendVerifyCode(String phone, String scene) {
        // 根据场景判断是否需要检查手机号是否已注册
        if ("register".equals(scene)) {
            // 校验手机号是否已注册
            User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, phone)
            );
            
            if (user != null) {
                return Result.<VerifyCodeVo>instance(409, "手机号已被注册，请更换手机号", null);
            }
        }
        
        // 生成6位数字验证码
        int code = 100000 + new Random().nextInt(900000);
        
        // 保存验证码到Redis，键格式：verify_code:场景:手机号
        String redisKey = VERIFY_CODE_PREFIX + scene + ":" + phone;
        redisTemplate.opsForValue().set(redisKey, String.valueOf(code), VERIFY_CODE_EXPIRE, TimeUnit.SECONDS);
        
        // 创建验证码视图对象
        VerifyCodeVo verifyCodeVo = new VerifyCodeVo(code, VERIFY_CODE_EXPIRE);
        
        // 实际应调用短信服务，这里直接返回验证码
        return Result.success("验证码发送成功", verifyCodeVo);
    }

    @Override
    public Result<?> checkVerifyCode(String phone, String verifyCode, String scene) {
        // 从Redis获取验证码
        String redisKey = VERIFY_CODE_PREFIX + scene + ":" + phone;
        String savedCode = redisTemplate.opsForValue().get(redisKey);
        
        // 验证码不存在或已过期
        if (savedCode == null) {
            return Result.instance(401, "验证码已过期", null);
        }
        
        // 验证码不匹配
        if (!savedCode.equals(verifyCode)) {
            return Result.instance(401, "验证码错误", null);
        }
        
        // 验证通过后删除Redis中的验证码，防止重复使用
        redisTemplate.delete(redisKey);
        
        // 返回成功结果，包含请求参数
        return Result.success("验证码校验成功", null);
    }
} 