package org.software.code.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * RedisUtil 是一个工具类，用于简化与 Redis 数据库的交互操作。
 * 通过使用 Spring Data Redis 提供的 StringRedisTemplate，该类封装了一些常用的 Redis 操作，
 * 如设置键值对、获取键对应的值以及删除键值对等，方便在项目中使用 Redis 进行数据缓存和存储。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Component
public class RedisUtil {

    // 注入 StringRedisTemplate 实例，用于与 Redis 数据库进行交互
    // StringRedisTemplate 是 Spring Data Redis 提供的一个模板类，专门用于处理字符串类型的键值对
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 向 Redis 中设置键值对的方法。
     * 使用 StringRedisTemplate 的 opsForValue() 方法获取操作字符串类型值的对象，
     * 然后调用其 set() 方法将指定的键和值存储到 Redis 中。
     *
     * @param key 要设置的键，类型为字符串
     * @param value 要设置的值，类型为字符串
     */
    public void setValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 向 Redis 中设置带过期时间的键值对
     *
     * @param key 要设置的键，类型为字符串
     * @param value 要设置的值，类型为字符串
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void setValue(String key, String value, long timeout, java.util.concurrent.TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 从 Redis 中获取指定键对应值的方法。
     * 使用 StringRedisTemplate 的 opsForValue() 方法获取操作字符串类型值的对象，
     * 然后调用其 get() 方法根据指定的键从 Redis 中获取对应的值。
     *
     * @param key 要获取值的键，类型为字符串
     * @return 如果键存在，返回该键对应的值；如果键不存在，返回 null
     */
    public String getValue(String key) {
        // 调用 StringRedisTemplate 的 opsForValue() 方法获取操作字符串类型值的对象
        // 再调用其 get() 方法根据键从 Redis 中获取对应的值
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 从 Redis 中删除指定键值对的方法。
     * 使用 StringRedisTemplate 的 delete() 方法根据指定的键从 Redis 中删除对应的键值对。
     *
     * @param key 要删除的键，类型为字符串
     */
    public void deleteValue(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 检查Token是否在黑名单中
     *
     * @param tokenHash token的哈希值
     * @return 是否在黑名单中
     */
    public boolean isTokenBlacklisted(String tokenHash) {
        try {
            String blacklistKey = "jwt:blacklist:" + tokenHash;
            String value = getValue(blacklistKey);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Token添加到黑名单
     *
     * @param tokenHash token的哈希值
     * @param userId 用户ID
     * @param ttlSeconds TTL秒数
     */
    public void addTokenToBlacklist(String tokenHash, Long userId, long ttlSeconds) {
        try {
            String blacklistKey = "jwt:blacklist:" + tokenHash;
            String value = userId + ":" + System.currentTimeMillis();
            if (ttlSeconds > 0) {
                setValue(blacklistKey, value, ttlSeconds, java.util.concurrent.TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            // 静默处理错误
        }
    }
}