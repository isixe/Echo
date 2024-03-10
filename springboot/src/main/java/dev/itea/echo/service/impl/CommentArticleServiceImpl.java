package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CommentArticle;
import dev.itea.echo.mapper.CommentArticleMapper;
import dev.itea.echo.service.CommentArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.ChildCommentVO;
import dev.itea.echo.vo.CommentArticleVO;
import dev.itea.echo.vo.RootCommentArticleVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 文章评论表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CommentArticleServiceImpl extends ServiceImpl<CommentArticleMapper, CommentArticle> implements CommentArticleService {

    @Resource
    CommentArticleMapper commentArticleMapper;

    @Override
    @Cacheable(cacheNames = "commentArticle", key = "#id")
    public CommentArticle get(Integer id) {
        return commentArticleMapper.selectById(id);
    }

    @Override
    @CachePut(cacheNames = "commentArticle", key = "#commentArticle.id")
    public CommentArticle update(CommentArticle commentArticle) {
        commentArticleMapper.updateById(commentArticle);
        return commentArticle;
    }

    @Override
    @CacheEvict(cacheNames = "commentArticle", key = "#id")
    public void delete(Integer id) {
        commentArticleMapper.deleteById(id);
    }

    @Override
    public IPage<CommentArticleVO> getPage(Pageable pageable, String keyword) {
        Page<CommentArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<CommentArticleVO> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("ca.content", keyword)
                    .or()
                    .like("u.name", keyword)
                    .or()
                    .like("a.title", keyword);
        }
        return commentArticleMapper.getArticleCommentByPage(page, wrapper);
    }

    @Override
    public List<RootCommentArticleVO> getRootListByArticleId(Integer id) {
        return commentArticleMapper.getRootListByArticleId(new QueryWrapper<RootCommentArticleVO>()
                .eq("ca.article_id", id).isNull("ca.root_comment_id"));
    }

    @Override
    public List<ChildCommentVO> getChildListByArticleId(Integer rootId) {
        return commentArticleMapper.getChildListByArticleId(new QueryWrapper<ChildCommentVO>()
                .eq("ca.root_comment_id", rootId).isNotNull("ca.root_comment_id"));
    }
}
