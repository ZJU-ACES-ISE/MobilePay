package org.software.code.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtil 是一个工具类，用于简化与 Redis 数据库的交互操作。
 * 通过使用 Spring Data Redis 提供的 StringRedisTemplate，该类封装了一些常用的 Redis 操作，
 * 如设置键值对、获取键对应的值以及删除键值对等，方便在项目中使用 Redis 进行数据缓存和存储。
 */
@Component
public class RedisUtil {

    // 注入 StringRedisTemplate 实例，用于与 Redis 数据库进行交互
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    // Token黑名单前缀
    public static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * 向 Redis 中设置键值对的方法。
     *
     * @param key 要设置的键，类型为字符串
     * @param value 要设置的值，类型为字符串
     */
    public void setValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 向 Redis 中设置带过期时间的键值对的方法。
     *
     * @param key 要设置的键，类型为字符串
     * @param value 要设置的值，类型为字符串
     * @param timeout 过期时间，单位为毫秒
     */
    public void setValue(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 从 Redis 中获取指定键对应值的方法。
     *
     * @param key 要获取值的键，类型为字符串
     * @return 如果键存在，返回该键对应的值；如果键不存在，返回 null
     */
    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    
    /**
     * 检查键是否存在
     * 
     * @param key 要检查的键
     * @return 如果键存在返回true，否则返回false
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 从 Redis 中删除指定键值对的方法。
     *
     * @param key 要删除的键，类型为字符串
     */
    public void deleteValue(String key) {
        stringRedisTemplate.delete(key);
    }
    
    /**
     * 检查token是否在黑名单中
     * 
     * @param token JWT token字符串
     * @return 如果token在黑名单中返回true，否则返回false
     */
    public boolean isTokenBlacklisted(String token) {
        return hasKey(TOKEN_BLACKLIST_PREFIX + token);
    }
} 