package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.UserRankVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 文章表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface ArticleService extends IService<Article> {

    void delete(Integer id);

    Article update(Article article);

    ArticleVO get(Integer id);

    List<UserRankVO> getUserArticleNumRankList();

    IPage<ArticleVO> getArticleByPage(Pageable pageable, String keyword);

    IPage<ArticleVO> getActiveArticleByPage(Pageable pageable, String keyword);

    IPage<ArticleVO> getActiveHotArticleByPage(Pageable pageable, String keyword);
}
