package dev.itea.echo.service;

import dev.itea.echo.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author isixe
 * @since 2024-03-13
 */
public interface FollowService extends IService<Follow> {

    Follow get(Integer id);

    void delete(Integer id);
}
