package dev.itea.echo.service.impl;

import dev.itea.echo.entity.QuestionThumb;
import dev.itea.echo.mapper.QuestionThumbMapper;
import dev.itea.echo.service.QuestionThumbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 问题点赞表 实现类
 *
 * @author isixe
 * @since 2024-02-27
 */
@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb> implements QuestionThumbService {

    @Resource
    QuestionThumbMapper questionThumbMapper;

    @Override
    @Cacheable(cacheNames = "questionThumb", key = "#id")
    public QuestionThumb get(Integer id) {
        return questionThumbMapper.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "questionThumb", key = "#id")
    public void delete(Integer id) {
        questionThumbMapper.deleteById(id);
    }
}
