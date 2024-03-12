package dev.itea.echo.service;

import dev.itea.echo.entity.ArticleThumb;
import dev.itea.echo.entity.QuestionThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问答点赞表 服务类
 *
 * @author isixe
 * @since 2024-02-27
 */
public interface QuestionThumbService extends IService<QuestionThumb> {

    QuestionThumb get(Integer id);

    void delete(Integer id);
}
