package dev.itea.echo.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springdoc openapi 配置
 *
 * @author: isixe
 * @create: 2024-01-14 13:46
 **/
@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI springdocOpenApi() {
        return new OpenAPI()
                //内部文档
                .info(new Info()
                        //标题
                        .title("SpringBoot RESTful API 接口文档")
                        //描述
                        .description("SpringBoot API 接口，Springdoc OpenAPI + Swagger3")
                        //API版本
                        .version("v1.0")
                        //联系方式
                        .contact(new Contact().name("isixe")
                                .url("http://localhost:8080/swagger-ui/index.html")
                                .email("isixe@outlook.com"))
                        //License
                        .license(new License()
                                .name("The Apache License, Version 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                //外部文档
                .externalDocs(new ExternalDocumentation()
                        //描述
                        .description("官方文档")
                        //链接
                        .url("http://springdoc.org"));
    }
}
