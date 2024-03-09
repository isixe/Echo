package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Article;
import dev.itea.echo.entity.Question;
import dev.itea.echo.mapper.QuestionMapper;
import dev.itea.echo.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
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
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<QuestionVO>().eq("q.is_deleted", 0);

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
        wrapper.orderByDesc("q.update_time");

        return questionMapper.getQuestionByPage(page, wrapper);
    }

    @Override
    public IPage<QuestionVO> getActiveQuestionByPage(Pageable pageable, String keyword, String sort) {
        Page<QuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<QuestionVO>()
                .eq("status", 1)
                .eq("q.is_deleted", 0);

        if (ObjectUtils.isEmpty(keyword)) {
            return questionMapper.getActiveQuestionByPage(page, wrapper.orderByDesc("q.update_time"));
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

        QueryWrapper<QuestionVO> sortedWrapper = Optional.ofNullable(sort)
                .map(s -> switch (s) {
                    case "likeCount" -> wrapper.orderByDesc("like_count");
                    case "updateTime" -> wrapper.orderByDesc("q.update_time");
                    default -> wrapper.orderByDesc("q.`pv_count`")
                            .orderByDesc("like_count")
                            .orderByDesc("q.update_time");
                })
                .orElse(wrapper);

        return questionMapper.getActiveQuestionByPage(page, sortedWrapper);
    }

    @Override
    public IPage<QuestionVO> getActiveHotArticleByPage(Pageable pageable, String keyword) {
        Page<QuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1)
                .eq("q.is_deleted", 0)
                .orderByDesc("q.`pv_count`")
                .orderByDesc("like_count")
                .orderByDesc("q.update_time");
        return questionMapper.getActiveQuestionByPage(page, wrapper);
    }

    @Override
    public List<UserRankVO> getUserQuestionNumRankList() {
        return questionMapper.getUserQuestionNumRankList();
    }

    @Override
    public List<Question> getDraftListByUserId(Integer userId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<Question>().eq("user_id", userId).eq("status", 0);
        return questionMapper.selectList(wrapper);
    }

    @Override
    public IPage<QuestionVO> getPageByUserId(Pageable pageable, Integer userId) {
        Page<QuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1).like("u.id", userId);
        return questionMapper.getQuestionByPage(page, wrapper);
    }

    @Override
    public IPage<QuestionVO> getPageByCategoryId(Pageable pageable, Integer categoryId) {
        Page<QuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1).eq("c.id", categoryId);
        return questionMapper.getActiveQuestionByPage(page, wrapper);
    }

    @Override
    public IPage<QuestionVO> getPageByTagName(Pageable pageable, String tagName) {
        Page<QuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<QuestionVO> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("status", 1).like("tag", tagName);
        return questionMapper.getActiveQuestionByPage(page, wrapper);
    }

    @Override
    public List<Question> getArticleListByTitle(String title) {
        return questionMapper.getQuestionByTitle(title);
    }
}
