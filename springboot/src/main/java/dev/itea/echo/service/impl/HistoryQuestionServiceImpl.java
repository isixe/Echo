package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.HistoryQuestion;
import dev.itea.echo.mapper.HistoryQuestionMapper;
import dev.itea.echo.service.HistoryQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.HistoryArticleVO;
import dev.itea.echo.vo.HistoryQuestionVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 用户问题浏览历史表 服务实现类
 *
 * @author isixe
 * @since 2024-03-18
 */
@Service
public class HistoryQuestionServiceImpl extends ServiceImpl<HistoryQuestionMapper, HistoryQuestion> implements HistoryQuestionService {

    @Resource
    HistoryQuestionMapper historyQuestionMapper;

    @Override
    @CacheEvict(cacheNames = "historyQuestion", key = "#id")
    public void delete(Integer id) {
        historyQuestionMapper.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "historyQuestion", key = "#historyQuestion.id")
    public HistoryQuestion update(HistoryQuestion historyQuestion) {
        historyQuestionMapper.updateById(historyQuestion);
        return historyQuestion;
    }

    @Override
    @Cacheable(value = "historyQuestion", key = "#id")
    public HistoryQuestion get(Integer id) {
        return historyQuestionMapper.selectById(id);
    }

    @Override
    public IPage<HistoryQuestionVO> getPageByUserId(Pageable pageable, String keyword, Integer userId) {
        Page<HistoryQuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<HistoryQuestionVO> wrapper = new QueryWrapper<HistoryQuestionVO>()
                .eq("hq.is_deleted", 0)
                .eq("q.is_deleted", 0)
                .eq("hq.user_id", userId)
                .orderByDesc("hq.update_time");

        return historyQuestionMapper.getPageByUserId(page, wrapper);
    }
}
