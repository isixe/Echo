package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.CommentArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.CommentArticleVO;
import org.springframework.data.domain.Pageable;

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
}
