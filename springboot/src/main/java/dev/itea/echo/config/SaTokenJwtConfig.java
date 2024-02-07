package dev.itea.echo.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import dev.itea.echo.entity.StpUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-token 集成 jwt 配置
 *
 * @author: isixe
 * @create: 2024-01-20 17:56
 **/
@Configuration
public class SaTokenJwtConfig {

    /**
     * set jwt mode
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * multi account support
     */
    @Autowired
    public void setUserStpLogic() {

        StpUserUtil.setStpLogic(new StpLogicJwtForSimple(StpUserUtil.TYPE){
            @Override
            public String splicingKeyTokenName() {
                return super.splicingKeyTokenName()+ "user";
            }
        });
    }
}
