package dev.itea.echo.service;

import dev.itea.echo.entity.ReactionCommentArticle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章评论反应表 服务类
 *
 * @author isixe
 * @since 2024-03-16
 */
public interface ReactionCommentArticleService extends IService<ReactionCommentArticle> {

    ReactionCommentArticle update(ReactionCommentArticle reactionCommentArticle);

    void delete(Integer id);

    ReactionCommentArticle get(Integer id);

}
