package dev.itea.echo;

import cn.dev33.satoken.SaManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

    @Test
    void contextLoads() {
        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
    }

    @Test
    void datasourceLoads() {
        //查看数据源
        log.info(String.valueOf(dataSource.getClass()));
        //获得数据库连接
        try {
            Connection connection = dataSource.getConnection();
            log.info(String.valueOf(connection));
            //关闭连接
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
