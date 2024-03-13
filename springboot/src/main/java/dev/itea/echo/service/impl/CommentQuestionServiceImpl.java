package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CommentQuestion;
import dev.itea.echo.mapper.CommentQuestionMapper;
import dev.itea.echo.service.CommentQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.*;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 问答评论表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CommentQuestionServiceImpl extends ServiceImpl<CommentQuestionMapper, CommentQuestion> implements CommentQuestionService {

    @Resource
    CommentQuestionMapper commentQuestionMapper;

    @Override
    @Cacheable(cacheNames = "commentQuestion", key = "#id")
    public CommentQuestion get(Integer id) {
        return commentQuestionMapper.selectById(id);
    }

    @Override
    @CachePut(cacheNames = "commentQuestion", key = "#commentQuestion.id")
    public CommentQuestion update(CommentQuestion commentQuestion) {
        commentQuestionMapper.updateById(commentQuestion);
        return commentQuestion;
    }

    @Override
    @CacheEvict(cacheNames = "commentQuestion", key = "#id")
    public void delete(Integer id) {
        commentQuestionMapper.deleteById(id);
    }

    @Override
    public IPage<CommentQuestionVO> getPage(Pageable pageable, String keyword) {
        Page<RootCommentQuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<RootCommentQuestionVO> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("cq.content", keyword)
                    .or()
                    .like("u.name", keyword)
                    .or()
                    .like("q.title", keyword);
        }
        return commentQuestionMapper.getPage(page, wrapper);
    }

    @Override
    public List<ChildCommentVO> getChildListByQuestionId(Integer rootId) {
        return commentQuestionMapper.getChildListByQuestionId(new QueryWrapper<ChildCommentVO>()
                .eq("cq.root_comment_id", rootId)
                .isNotNull("cq.root_comment_id"));
    }

    @Override
    public IPage<RootCommentQuestionVO> getPageWithRootCommentByQuestionId(Pageable pageable, Integer questionId) {
        Page<RootCommentQuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<RootCommentQuestionVO> wrapper = new QueryWrapper<RootCommentQuestionVO>()
                .eq("cq.is_deleted", 0)
                .eq("cq.question_id", questionId)
                .isNull("cq.root_comment_id")
                .orderByDesc("cq.created_time");
        return commentQuestionMapper.getPageWithRootCommentByQuestionId(page, wrapper);
    }
}
