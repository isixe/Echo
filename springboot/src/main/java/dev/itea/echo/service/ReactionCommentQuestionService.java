package dev.itea.echo.service;

import dev.itea.echo.entity.ReactionCommentQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问答评论反应表 服务类
 *
 * @author isixe
 * @since 2024-03-16
 */
public interface ReactionCommentQuestionService extends IService<ReactionCommentQuestion> {

    ReactionCommentQuestion update(ReactionCommentQuestion reactionCommentQuestion);

    void delete(Integer id);

    ReactionCommentQuestion get(Integer id);

}
