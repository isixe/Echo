package dev.itea.echo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mybatis Plus 自动填充策略配置
 *
 * @author: isixe
 * @create: 2023-05-24 19:53
 **/
@Slf4j
@Component
public class AutoFieldFillMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ......");
        this.strictInsertFill(metaObject, "lastActiveTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime::now, LocalDateTime.class);
        log.info("end insert fill ......");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ......");
        this.strictInsertFill(metaObject, "lastActiveTime", LocalDateTime::now, LocalDateTime.class);
        log.info("end update fill ......");
    }
}

