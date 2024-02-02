package dev.itea.echo.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * MybatisPlusGenerator
 *
 * @author: isixe
 * @create: 2023-06-14 12:13
 **/
public class MybatisPlusGenerator {
    private final String url;
    private final String username;
    private final String password;
    private final String PACKAGE_NAME = this.getClass().getPackage().getName().substring(0, this.getClass().getPackage().getName().lastIndexOf("."));
    private final Map<OutputFile, String> pathInfo = Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "\\src\\main\\resources\\mapper");

    /**
     * Consumer Builder
     */
    private final Consumer<TemplateConfig.Builder> templateConfig = builder -> builder
            .entity("templates/myentity.java");

    private final Consumer<GlobalConfig.Builder> globalConfig = builder -> builder
            .outputDir(System.getProperty("user.dir") + "\\src\\main\\java")
            .enableSwagger()
            .disableOpenDir()
            .commentDate("yyyy-MM-dd");

    private final Consumer<StrategyConfig.Builder> strategyConfig = builder -> builder
            // Public
            .addTablePrefix("t_", "p_")
            // Entity
            .entityBuilder()
            .enableLombok()
            .enableTableFieldAnnotation()
            .enableChainModel()
            .enableActiveRecord()
            .enableLombok()
            .logicDeleteColumnName("deleted")
            .enableTableFieldAnnotation()
            // Service
            .serviceBuilder()
            .formatServiceFileName("%sService")
            .formatServiceImplFileName("%sServiceImpl")
            // Controller
            .controllerBuilder()
            .formatFileName("%sController")
            .enableHyphenStyle()
            .enableRestStyle()
            // Mapper
            .mapperBuilder()
            .formatMapperFileName("%sMapper")
            .formatXmlFileName("%sMapper")
            .superClass(BaseMapper.class)
            .enableBaseResultMap();

    public MybatisPlusGenerator(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * FastAutoGenerator
     *
     * @param url            database driver url
     * @param username       database username
     * @param password       database password
     * @param author         generate by author
     * @param pathInfo       mapper resource
     * @param templateConfig template config
     * @return
     */
    public FastAutoGenerator getAutoGenerator(String url, String username, String password, String author, Map<OutputFile, String> pathInfo, Consumer<TemplateConfig.Builder> templateConfig) {
        return FastAutoGenerator.create(url, username, password)
                .globalConfig(globalConfig)
                .globalConfig(builder -> builder.author(author))
                .packageConfig(builder -> builder
                        .parent(PACKAGE_NAME)
                        .moduleName("")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller")
                        .mapper("mapper")
                        .xml("mapper")
                        .pathInfo(pathInfo))
                .strategyConfig(strategyConfig)
                .templateConfig(templateConfig)
                .templateEngine(new FreemarkerTemplateEngine());
    }

    /**
     * Generate one table over
     *
     * @param table  database table name
     * @param author generate by author
     */
    public void generateEntity(String table, String author) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(globalConfig)
                .globalConfig(builder -> builder.author(author).fileOverride())
                .packageConfig(builder -> builder
                        .parent(PACKAGE_NAME)
                        .moduleName("")
                        .entity("entity")
                        .mapper("mapper")
                        .xml("mapper")
                        .pathInfo(pathInfo))

                .strategyConfig(builder -> builder
                        .addInclude(table)
                        .entityBuilder().enableFileOverride()
                        .mapperBuilder().enableFileOverride())
                .strategyConfig(strategyConfig)
                .templateConfig(templateConfig)
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    /**
     * Generate one table
     *
     * @param table  database table name
     * @param author generate by author
     */
    public void generate(String table, String author) {
        getAutoGenerator(url, username, password, author, pathInfo, templateConfig).strategyConfig(builder -> builder.addInclude(table)).execute();
    }

    /**
     * Generate all table
     *
     * @param tables database table name list
     * @param author generate by author
     */
    public void generateAll(List<String> tables, String author) {
        getAutoGenerator(url, username, password, author, pathInfo, templateConfig).strategyConfig(builder -> builder.addInclude(tables)).execute();
    }
}

