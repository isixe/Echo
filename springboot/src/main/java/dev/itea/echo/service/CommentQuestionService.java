package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.CommentQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.ChildCommentVO;
import dev.itea.echo.vo.CommentQuestionVO;
import dev.itea.echo.vo.RootCommentQuestionVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 问答评论表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CommentQuestionService extends IService<CommentQuestion> {

    CommentQuestion get(Integer id);

    CommentQuestion update(CommentQuestion commentQuestion);

    void delete(Integer id);

    List<RootCommentQuestionVO> getRootListByQuestionId(Integer questionId);

    List<ChildCommentVO> getChildListByQuestionId(Integer rootId);

    IPage<CommentQuestionVO> getQuestionByPage(Pageable pageable, String keyword);
}
