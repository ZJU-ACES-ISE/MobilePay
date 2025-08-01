package org.software.code.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfig 是 Redis 配置类，用于配置 Redis 相关的 Bean
 *
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置RedisTemplate
     *
     * @param redisConnectionFactory Redis连接工厂
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        return redisTemplate;
    }

    /**
     * 创建并配置一个 StringRedisTemplate 实例。
     * StringRedisTemplate 是 Spring Data Redis 提供的一个用于操作 Redis 的模板类，
     * 专门用于处理字符串类型的键值对，方便在代码中进行 Redis 操作。
     *
     * @param redisConnectionFactory Redis 连接工厂，用于创建与 Redis 服务器的连接。
     * @return 配置好的 StringRedisTemplate 实例。
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 使用传入的 Redis 连接工厂创建一个 StringRedisTemplate 实例
        return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * 创建并配置一个 RedisCacheManager 实例。
     * RedisCacheManager 是 Spring Data Redis 提供的缓存管理器，用于管理 Redis 缓存。
     *
     * @param connectionFactory Redis 连接工厂，用于创建与 Redis 服务器的连接。
     * @return 配置好的 RedisCacheManager 实例。
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 获取 Redis 缓存的默认配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存值的序列化方式为使用 GenericJackson2JsonRedisSerializer，
                // 该序列化器可以将对象序列化为 JSON 格式存储在 Redis 中，方便数据的存储和读取。
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 使用 RedisCacheManager 的构建器模式，传入 Redis 连接工厂和缓存默认配置，构建并返回一个 RedisCacheManager 实例
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}