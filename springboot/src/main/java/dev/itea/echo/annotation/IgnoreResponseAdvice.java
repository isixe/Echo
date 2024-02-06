package dev.itea.echo.annotation;

import java.lang.annotation.*;

/**
 * @author: isixe
 * @create: 2023-06-22 19:37
 * @description: 取消全局响应体封装
 **/
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {

}
