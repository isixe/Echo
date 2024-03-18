package dev.itea.echo.service.impl;

import dev.itea.echo.entity.ReactionCommentQuestion;
import dev.itea.echo.mapper.ReactionCommentQuestionMapper;
import dev.itea.echo.service.ReactionCommentQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 问答评论反应表 服务实现类
 *
 * @author isixe
 * @since 2024-03-16
 */
@Service
public class ReactionCommentQuestionServiceImpl extends ServiceImpl<ReactionCommentQuestionMapper, ReactionCommentQuestion> implements ReactionCommentQuestionService {

    @Resource
    ReactionCommentQuestionMapper reactionCommentQuestionMapper;

    @Override
    @CachePut(cacheNames = "reactionCommentQuestion", key = "#reactionCommentQuestion.id")
    public ReactionCommentQuestion update(ReactionCommentQuestion reactionCommentQuestion) {
        reactionCommentQuestionMapper.updateById(reactionCommentQuestion);
        return reactionCommentQuestion;
    }

    @Override
    @CacheEvict(cacheNames = "reactionCommentQuestion", key = "#id")
    public void delete(Integer id) {
        reactionCommentQuestionMapper.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "reactionCommentQuestion", key = "#id")
    public ReactionCommentQuestion get(Integer id) {
        return reactionCommentQuestionMapper.selectById(id);
    }
}
