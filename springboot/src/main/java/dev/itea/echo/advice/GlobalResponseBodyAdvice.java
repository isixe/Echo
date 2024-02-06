package dev.itea.echo.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.itea.echo.annotation.IgnoreResponseAdvice;
import dev.itea.echo.entity.result.ErrorResult;
import dev.itea.echo.entity.result.Result;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: isixe
 * @create: 2023-06-20 23:43
 * @description: 拦截全局Controller返回值
 **/
@RestControllerAdvice(basePackages = "dev.itea.echo.controller")
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.hasMethodAnnotation(IgnoreResponseAdvice.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (o instanceof String) {
            return objectMapper.writeValueAsString(Result.success(o));
        }
        if (o instanceof Result || o instanceof ErrorResult) {
            return o;
        }
        return Result.success(o);
    }
}
