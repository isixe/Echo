package dev.itea.echo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域处理配置
 *
 * @author: isixe
 * @create: 2023-06-27 19:56
 **/
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                //是否开启cookie跨域
                .allowCredentials(false)
                //规定能够跨域访问的方法类型
                .allowedMethods("GET","POST","DELETE","PUT","OPTIONS")
                //添加验证头信息  token
                //.allowedHeaders()
                //预检请求存活时间 在此期间不再次发送预检请求
                .maxAge(3600);
    }
}
