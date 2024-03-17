package dev.itea.echo.service.impl;

import dev.itea.echo.entity.ReactionCommentArticle;
import dev.itea.echo.mapper.ReactionCommentArticleMapper;
import dev.itea.echo.service.ReactionCommentArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 文章评论反应表 服务实现类
 *
 * @author isixe
 * @since 2024-03-16
 */
@Service
public class ReactionCommentArticleServiceImpl extends ServiceImpl<ReactionCommentArticleMapper, ReactionCommentArticle> implements ReactionCommentArticleService {

    @Resource
    ReactionCommentArticleMapper reactionCommentArticleMapper;

    @Override
    @CachePut(cacheNames = "reactionCommentArticle", key = "#reactionCommentArticle.id")
    public ReactionCommentArticle update(ReactionCommentArticle reactionCommentArticle) {
        reactionCommentArticleMapper.updateById(reactionCommentArticle);
        return reactionCommentArticle;
    }

    @Override
    @CacheEvict(cacheNames = "reactionCommentArticle", key = "#id")
    public void delete(Integer id) {
        reactionCommentArticleMapper.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "reactionCommentArticle", key = "#id")
    public ReactionCommentArticle get(Integer id) {
        return reactionCommentArticleMapper.selectById(id);
    }

}
