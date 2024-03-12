package dev.itea.echo.service.impl;

import dev.itea.echo.entity.ArticleThumb;
import dev.itea.echo.mapper.ArticleThumbMapper;
import dev.itea.echo.service.ArticleThumbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 文章点赞表 服务实现类
 *
 * @author isixe
 * @since 2024-02-27
 */
@Service
public class ArticleThumbServiceImpl extends ServiceImpl<ArticleThumbMapper, ArticleThumb> implements ArticleThumbService {

    @Resource
    ArticleThumbMapper articleThumbMapper;

    @Override
    @Cacheable(cacheNames = "articleThumb", key = "#id")
    public ArticleThumb get(Integer id) {
        return articleThumbMapper.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "articleThumb", key = "#id")
    public void delete(Integer id) {
        articleThumbMapper.deleteById(id);
    }

}
