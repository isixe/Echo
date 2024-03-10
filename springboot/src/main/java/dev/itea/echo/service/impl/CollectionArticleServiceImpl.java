package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CollectionArticle;
import dev.itea.echo.mapper.CollectionArticleMapper;
import dev.itea.echo.service.CollectionArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.CollectionArticleVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 收藏表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CollectionArticleServiceImpl extends ServiceImpl<CollectionArticleMapper, CollectionArticle> implements CollectionArticleService {

    @Resource
    CollectionArticleMapper collectionArticleMapper;

    @Override
    @CacheEvict(cacheNames = "collectionArticle", key = "#id")
    public void delete(Integer id) {
        collectionArticleMapper.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "collectionArticle", key = "#id")
    public CollectionArticle get(Integer id) {
        return collectionArticleMapper.selectById(id);
    }

    @Override
    public IPage<CollectionArticleVO> getPage(Pageable pageable, String keyword) {
        Page<CollectionArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<CollectionArticleVO> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("u.name", keyword)
                    .or()
                    .like("a.title", keyword);
        }
        return collectionArticleMapper.getCollectionArticleByPage(page, wrapper);
    }

    @Override
    public IPage<CollectionArticleVO> getPageByUserId(Pageable pageable, Integer userId) {
        Page<CollectionArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<CollectionArticleVO> wrapper = new QueryWrapper<CollectionArticleVO>().eq("ca.user_id", userId);
        return collectionArticleMapper.getCollectionArticleByPage(page, wrapper);
    }
}
