package dev.itea.echo.service.impl;

import dev.itea.echo.entity.HistoryArticle;
import dev.itea.echo.mapper.HistoryArticleMapper;
import dev.itea.echo.service.HistoryArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户文章浏览历史表 服务实现类
 *
 * @author isixe
 * @since 2024-03-18
 */
@Service
public class HistoryArticleServiceImpl extends ServiceImpl<HistoryArticleMapper, HistoryArticle> implements HistoryArticleService {

    @Resource
    HistoryArticleMapper historyArticleMapper;

    @Override
    @CacheEvict(cacheNames = "historyArticle", key = "#id")
    public void delete(Integer id) {
        historyArticleMapper.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "historyArticle", key = "#id")
    public HistoryArticle get(Integer id) {
        return historyArticleMapper.selectById(id);
    }

}
