package dev.itea.echo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import dev.itea.echo.annotation.Sensitive;
import dev.itea.echo.entity.SensitiveStrategy;

import java.io.IOException;
import java.util.Objects;

/**
 * SensitiveJsonSerializer
 *
 * @author: isixe
 * @create: 2024-02-06 20:03
 **/
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveStrategy strategy;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(strategy.desensitizer().apply(s));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
