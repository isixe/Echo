package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @CacheEvict(cacheNames = "articleVO", key = "#id")
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
    public List<Article> getListByGroupId(Integer groupId) {
        return articleMapper.getListByGroupId(groupId);
    }

    @Override
    public List<UserRankVO> getUserArticleNumRankList() {
        return articleMapper.getUserArticleNumRankList();
    }

    @Override
    public IPage<ArticleVO> getArticleByPage(Pageable pageable, String keyword) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
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
        return articleMapper.getArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getActiveArticleByPage(Pageable pageable, String keyword) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1);

        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.eq("status", 1)
                    .like("title", keyword)
                    .or()
                    .like("content", keyword)
                    .or()
                    .like("u.name", keyword)
                    .or()
                    .like("category_name", keyword)
                    .or()
                    .like("tag", keyword);
        }
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getActiveHotArticleByPage(Pageable pageable, String keyword) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1);

        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.eq("status", 1)
                    .like("title", keyword)
                    .or()
                    .like("content", keyword)
                    .or()
                    .like("u.name", keyword)
                    .or()
                    .like("category_name", keyword)
                    .or()
                    .like("tag", keyword);
        }
        return articleMapper.getHotArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageByCategoryId(Pageable pageable, Integer categoryId) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1).eq("c.id", categoryId);
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageByTagName(Pageable pageable, String tagName) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1).like("tag", tagName);
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public IPage<ArticleVO> getPageByUserId(Pageable pageable, Integer userId) {
        Page<ArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<ArticleVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1).like("u.id", userId);
        return articleMapper.getActiveArticleByPage(page, wrapper);
    }

    @Override
    public List<Article> getArticleListByTitle(String title) {
        return articleMapper.getArticleByTitle(title);
    }


}
