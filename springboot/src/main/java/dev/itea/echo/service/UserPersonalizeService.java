package dev.itea.echo.service;

import dev.itea.echo.entity.UserPersonalize;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户个性化设置表 服务类
 *
 * @author isixe
 * @since 2024-03-26
 */
public interface UserPersonalizeService extends IService<UserPersonalize> {

    UserPersonalize get(Integer id);

    UserPersonalize update(UserPersonalize userPersonalize);
}
