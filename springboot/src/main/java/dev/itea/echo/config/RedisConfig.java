package dev.itea.echo.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * RedisTemplate 配置类
 *
 * @author: isixe
 * @create: 2024-02-20 18:22
 **/
@Configuration
@EnableCaching
public class RedisConfig {

    private final GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer;

    public RedisConfig() {
        ObjectMapper objectMapper = new ObjectMapper()
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //设置timeModule手动序列化
        JavaTimeModule timeModule = new JavaTimeModule();
        // LocalDateTime和LocalDate序列化
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.of("Asia/Shanghai"))))

                .addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                .withZone(ZoneId.of("Asia/Shanghai"))))

                .addSerializer(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                .withZone(ZoneId.of("Asia/Shanghai"))))

                .addDeserializer(LocalDate.class,
                        new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                .withZone(ZoneId.of("Asia/Shanghai"))));
        // 设置自定义时间模块
        objectMapper.registerModule(timeModule);

        this.jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
    }

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
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

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
        //配置redisCacheConfiguration缓存设置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(5000))
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))

                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer));

//                .disableCachingNullValues();

        //初始化RedisCacheManager实例
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

}