package dev.itea.echo.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: isixe
 * @create: 2023-06-15 21:13
 * @description: 启动 MybatisPlusGenerator
 **/
@SpringBootTest
@Slf4j
public class MybatisPlusGeneratorTests {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 单表生成
     */
    @Test
    void generatorload() {
        // 作者
        String author = "";
        //单表名称
        String tableName = "user";
        // 启动
        MybatisPlusGenerator generator = new MybatisPlusGenerator(dbUrl, dbUsername, dbPassword);
        generator.generate(tableName, author);

    }

    /**
     * 同步表单实体
     */
    @Test
    void generatorEntityload() {
        // 作者
        String author = "isixe";
        //单表名称
        String tableName = "role";
        // 启动
        MybatisPlusGenerator generator = new MybatisPlusGenerator(dbUrl, dbUsername, dbPassword);
        generator.generateEntity(tableName, author);
    }

    /**
     * 全表生成
     */
    @Test
    void generatorAllLoads() {
        // 作者
        String author = "isixe";
        // 准备要生成的数据表名列表
        List<String> tables = getAllTableNames("echo");
        // 启动
        MybatisPlusGenerator generator = new MybatisPlusGenerator(dbUrl, dbUsername, dbPassword);
        generator.generateAll(tables, author);
    }

    /**
     * 获取数据库的全部表名
     * @param databaseName
     * @return 数据库中的所有表名
     */
    public List<String> getAllTableNames(String databaseName) {
        List<String> tableNames = new ArrayList<>();
        String[] types = {"TABLE"};
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(databaseName, null, "%", types);
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }
}
