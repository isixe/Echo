package dev.itea.echo.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * RedisTemplate 配置类
 *
 * @author: isixe
 * @create: 2024-02-20 18:22
 **/
@Configuration
@EnableCaching
public class RedisConfig {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    /**
     * 初始化RedisConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * RedisTemplate配置与序列化
     */
    @Bean
    @ConditionalOnSingleCandidate
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        //初始化 jackson2Json redis Serializer，设置 ObjectMapper
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // String     - StringRedisSerializer 序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key        - StringRedisSerializer 序列化
        template.setKeySerializer(stringRedisSerializer);
        // hash       - StringRedisSerializer 序列化
        template.setHashKeySerializer(stringRedisSerializer);
        // value      - jackson2JsonRedisSerializer 序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash value - jackson2JsonRedisSerializer 序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置RedisCacheManager管理CacheManager缓存
     */
    @Bean
    public CacheManager cacheManager() {
        //初始化 jackson2Json redis Serializer，设置 ObjectMapper
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        //配置redisCacheConfiguration缓存设置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(5000))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)).disableCachingNullValues();

        //初始化RedisCacheManager实例
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

    /**
     * 自定义Redis键
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, params) -> {
            StringBuilder builder = new StringBuilder();
            builder.append(o.getClass().getName());
            builder.append("。");
            builder.append(method.getName());
            for (Object param : params) {
                builder.append(param.toString());
            }
            return builder.toString();
        };
    }

}