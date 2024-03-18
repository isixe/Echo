package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.HistoryArticle;
import dev.itea.echo.mapper.HistoryArticleMapper;
import dev.itea.echo.service.HistoryArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.HistoryArticleVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
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
    @CachePut(cacheNames = "historyArticle", key = "#historyArticle.id")
    public HistoryArticle update(HistoryArticle historyArticle) {
        historyArticleMapper.updateById(historyArticle);
        return historyArticle;
    }

    @Override
    @Cacheable(cacheNames = "historyArticle", key = "#id")
    public HistoryArticle get(Integer id) {
        return historyArticleMapper.selectById(id);
    }

    @Override
    public IPage<HistoryArticleVO> getPageByUserId(Pageable pageable, String keyword, Integer userId) {
        Page<HistoryArticleVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<HistoryArticleVO> wrapper = new QueryWrapper<HistoryArticleVO>()
                .eq("ha.is_deleted", 0)
                .eq("a.is_deleted", 0)
                .eq("ha.user_id", userId)
                .orderByDesc("ha.update_time");

        return historyArticleMapper.getPageByUserId(page, wrapper);
    }

}
