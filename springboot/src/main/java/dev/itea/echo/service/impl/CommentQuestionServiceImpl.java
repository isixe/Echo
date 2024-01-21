package dev.itea.echo.service.impl;

import dev.itea.echo.entity.CommentQuestion;
import dev.itea.echo.mapper.CommentQuestionMapper;
import dev.itea.echo.service.CommentQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 问答评论表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CommentQuestionServiceImpl extends ServiceImpl<CommentQuestionMapper, CommentQuestion> implements CommentQuestionService {

}
