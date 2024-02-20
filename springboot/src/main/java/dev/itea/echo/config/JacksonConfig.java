package dev.itea.echo.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Jackson null值转换空字符配置
 *
 * @author: isixe
 * @create: 2023-06-24 12:38
 * @description: null值转换空字符
 **/
@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String fieldName = jsonGenerator.getOutputContext().getCurrentName();
                //反射获取字段
                Class<?> currentClass = jsonGenerator.getCurrentValue().getClass();
                Field field = ReflectionUtils.findField(currentClass, fieldName);
                //判断空值
                if (ObjectUtils.isNull(field)) {
                    return;
                }
                //String类型返回""
                if (Objects.equals(Objects.requireNonNull(field).getType(), String.class)) {
                    jsonGenerator.writeString("");
                    return;
                }
                //Boolean类型返回false
                if (Objects.equals(field.getType(), Boolean.class)) {
                    jsonGenerator.writeBoolean(false);
                    return;
                }
                // 数字类型Integer、Float、Double、BigDecimal、Long、Short等类型返回0
                if (Number.class.isAssignableFrom(field.getType())) {
                    jsonGenerator.writeNumber(0);
                    return;
                }
                //Array、List、Set类型返回[]
                if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType())) {
                    jsonGenerator.writeStartArray();
                    jsonGenerator.writeEndArray();
                    return;
                }
                //Date类型返回null
                if (Date.class.isAssignableFrom(field.getType()) || LocalDateTime.class.isAssignableFrom(field.getType()) || LocalDate.class.isAssignableFrom(field.getType()) || LocalTime.class.isAssignableFrom(field.getType())) {
                    jsonGenerator.writeNull();
                    return;
                }

                //其他Object返回{}
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        });
        return objectMapper;
    }
}