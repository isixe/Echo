package dev.itea.echo.annotation;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.itea.echo.utils.StpUserUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SaUserCheckLogin
 *
 * @author: isixe
 * @create: 2024-02-07 13:13
 **/
@SaCheckLogin(type = StpUserUtil.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
public @interface SaUserCheckLogin {
}
