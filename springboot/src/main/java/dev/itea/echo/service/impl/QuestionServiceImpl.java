package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Question;
import dev.itea.echo.mapper.QuestionMapper;
import dev.itea.echo.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 问答表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    QuestionMapper questionMapper;

    @Override
    @CacheEvict(cacheNames = "question", key = "#id")
    public void delete(Integer id) {
        questionMapper.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = "question", key = "#question.id")
    public Question update(Question question) {
        questionMapper.updateById(question);
        return question;
    }

    @Override
    @Cacheable(cacheNames = "question", key = "#id")
    public QuestionVO get(Integer id) {
        return questionMapper.getById(id);
    }

    @Override
    public IPage<QuestionVO> getQuestionByPage(Pageable pageable, String keyword) {
        Page<QuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<>();
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
        return questionMapper.getQuestionByPage(page, wrapper);
    }
}
