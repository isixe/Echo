package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.CommentArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.ChildCommentVO;
import dev.itea.echo.vo.CommentArticleVO;
import dev.itea.echo.vo.RootCommentArticleVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 文章评论表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CommentArticleService extends IService<CommentArticle> {

    CommentArticle get(Integer id);

    CommentArticle update(CommentArticle commentArticle);

    void delete(Integer id);

    IPage<CommentArticleVO> getArticleByPage(Pageable pageable, String keyword);

    List<RootCommentArticleVO> getRootListByArticleId(Integer id);

    List<ChildCommentVO> getChildListByArticleId(Integer rootId);
}
