package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Article;
import dev.itea.echo.mapper.ArticleMapper;
import dev.itea.echo.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.UserRankVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * 文章表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    ArticleMapper articleMapper;

    @Override
    @CacheEvict(cacheNames = "article", key = "#id")
    public void delete(Integer id) {
        articleMapper.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = "article", key = "#article.id")
    public Article update(Article article) {
        articleMapper.updateById(article);
        return article;
    }

    @Override
    @Cacheable(cacheNames = "article", key = "#id")
    public ArticleVO get(Integer id) {
        return articleMapper.getById(id);
    }

    @Override
    public List<ArticleVO> getListByGroupId(Integer groupId) {
        return articleMapper.getListByGroupId(groupId);
    }

    @Override
    public List<UserRankVO> getUserArticleNumRankList() {
        return articleMapper.getUserArticleNumRankList();
    }

    @Override
    public IPage<ArticleVO> getPage(Pageable pageable, String keyword) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<ArticleVO>().eq("a.is_deleted", 0);

        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("title", keyword)
                    .or()
                    .like("content", keyword)
                    .or()
                    .like("u.name", keyword)
                    .or()
                    .like("category_name", keyword)
                    .or()
                    .like("tag", keyword);
        }
        wrapper.orderByDesc("a.update_time");

        return articleMapper.getArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageWithActive(Pageable pageable, String keyword, String sort) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<ArticleVO>()
                .eq("status", 1)
                .eq("a.is_deleted", 0);

        if (ObjectUtils.isEmpty(keyword)) {
            return articleMapper.getActiveArticleByPage(page, wrapper.orderByDesc("a.update_time"));
        }

        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper.and(qw -> qw
                    .like("title", keyword)
                    .or()
                    .like("content", keyword)
                    .or()
                    .like("u.name", keyword)
                    .or()
                    .like("category_name", keyword)
                    .or()
                    .like("tag", keyword));
        }

        QueryWrapper<ArticleVO> sortedWrapper = Optional.ofNullable(sort)
                .map(s -> switch (s) {
                    case "likeCount" -> wrapper.orderByDesc("like_count");
                    case "updateTime" -> wrapper.orderByDesc("a.update_time");
                    default -> wrapper.orderByDesc("a.`pv_count`")
                            .orderByDesc("like_count")
                            .orderByDesc("a.update_time");
                })
                .orElse(wrapper);

        return articleMapper.getActiveArticleByPage(page, sortedWrapper);
    }


    @Override
    public IPage<ArticleVO> getPageWithHotActive(Pageable pageable, String keyword) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();

        wrapper = wrapper.eq("status", 1)
                .eq("a.is_deleted", 0)
                .orderByDesc("a.`pv_count`")
                .orderByDesc("like_count")
                .orderByDesc("a.update_time");

        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageByCategoryId(Pageable pageable, Integer categoryId) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1)
                .eq("c.id", categoryId)
                .eq("a.is_deleted", 0)
                .orderByDesc("a.update_time");

        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageByTagName(Pageable pageable, String tagName) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<ArticleVO>()
                .eq("status", 1)
                .like("tag", tagName)
                .eq("a.is_deleted", 0)
                .orderByDesc("a.update_time");
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageByUserId(Pageable pageable, Integer userId) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<ArticleVO>()
                .eq("status", 1)
                .eq("u.id", userId)
                .eq("a.is_deleted", 0)
                .orderByDesc("a.update_time");
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public List<Article> getListWithDraftByUserId(Integer userId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
                .eq("user_id", userId)
                .eq("status", 0);
        return articleMapper.selectList(wrapper);
    }

    @Override
    @CacheEvict(cacheNames = "article", key = "#id")
    public void updateArticleGroupId(Integer id, Integer groupId) {
        UpdateWrapper<Article> wrapper = new UpdateWrapper<Article>()
                .eq("id", id)
                .set("article_group_id", groupId);
        articleMapper.update(null, wrapper);
    }

    @Override
    @CacheEvict(cacheNames = "article", key = "#id")
    public void deleteArticleGroupId(Integer id) {
        UpdateWrapper<Article> wrapper = new UpdateWrapper<Article>()
                .eq("id", id)
                .set("article_group_id", null);
        articleMapper.update(null, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageWithUnGroupByUserId(Pageable pageable, Integer userId) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<ArticleVO>()
                .eq("status", 1)
                .eq("u.id", userId)
                .isNull("article_group_id")
                .eq("a.is_deleted", 0)
                .orderByDesc("a.update_time");
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public List<Article> getListByTitle(String title) {
        return articleMapper.getArticleByTitle(title);
    }
}
