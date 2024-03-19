package dev.itea.echo.service;

import dev.itea.echo.entity.ArticleThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 *
 * @author isixe
 * @since 2024-02-27
 */
public interface ArticleThumbService extends IService<ArticleThumb> {

    ArticleThumb get(Integer id);

    void delete(Integer id);
}
