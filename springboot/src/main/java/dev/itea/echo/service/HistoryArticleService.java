package dev.itea.echo.service;

import dev.itea.echo.entity.HistoryArticle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户文章浏览历史表 服务类
 *
 * @author isixe
 * @since 2024-03-18
 */
public interface HistoryArticleService extends IService<HistoryArticle> {

    void delete(Integer id);

    HistoryArticle get(Integer id);

}
