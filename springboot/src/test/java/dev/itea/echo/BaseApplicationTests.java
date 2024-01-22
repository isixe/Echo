package dev.itea.echo;

import cn.dev33.satoken.SaManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

/**
 * @author: isixe
 * @create: 2023-06-14 21:13
 * @description: 数据库连接测试
 **/
@SpringBootTest
@Slf4j
class BaseApplicationTests {
    @Resource
    private DataSource dataSource;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        log.info("Sa-Token init: " + SaManager.getConfig());
    }

    /**
     * MySQL Test
     */
    @Test
    void datasourceLoads() {
        //check datasource
        log.info("MySQL dataSource init: " + dataSource.getClass());
        try {
            //get connection
            Connection connection = dataSource.getConnection();
            log.info("MySQL connection: " + connection);
            //close
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redis Test
     */
    @Test
    void redisLoads() {
        //set redis
        log.info("Redis Template init: " + stringRedisTemplate);
        stringRedisTemplate.opsForValue().set("redis", "Redis init success", Duration.ofMinutes(1));
        //get
        String value = this.stringRedisTemplate.opsForValue().get("redis");
        log.info("Init Result={}", value);
    }
}
