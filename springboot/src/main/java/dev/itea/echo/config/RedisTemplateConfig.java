package dev.itea.echo.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate 配置类，实现序列化
 *
 * @author: isixe
 * @create: 2024-02-20 18:22
 **/
@Configuration
public class RedisTemplateConfig {
    @Bean
    @ConditionalOnSingleCandidate
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //初始化 jackson2Json redis Serializer，设置 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
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
}