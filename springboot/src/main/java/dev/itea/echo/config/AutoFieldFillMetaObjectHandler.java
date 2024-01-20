package dev.itea.echo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: isixe
 * @create: 2023-05-24 19:53
 * @description: 重写自动填充策略
 **/
@Slf4j
@Component
public class AutoFieldFillMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ......");
        this.strictInsertFill(metaObject, "last_active_time", LocalDateTime.class, LocalDateTime.now());
        log.info("end insert fill ......");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ......");
        this.strictInsertFill(metaObject, "last_active_time", LocalDateTime.class, LocalDateTime.now());
        log.info("end update fill ......");
    }
}

