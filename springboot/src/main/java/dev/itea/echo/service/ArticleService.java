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

    void updateArticleGroupId(Integer id, Integer groupId);

    void deleteArticleGroupId(Integer id);

    List<ArticleVO> getListByGroupId(Integer groupId);

    List<UserRankVO> getListWithUserNumRank();

    IPage<ArticleVO> getPage(Pageable pageable, String keyword);

    IPage<ArticleVO> getPageWithActive(Pageable pageable, String keyword, String sort);

    IPage<ArticleVO> getPageWithActiveByCategoryId(Pageable pageable, Integer categoryId);

    IPage<ArticleVO> getPageWithActiveByTagName(Pageable pageable, String tagName);

    IPage<ArticleVO> getPageWithActiveByUserId(Pageable pageable, Integer userId);

    IPage<ArticleVO> getPageWithActiveAndUnGroupByUserId(Pageable pageable, Integer userId);

    IPage<Article> getPageWithActiveByTitle(Pageable pageable, String title);

    IPage<Article> getPageWithDraftByUserId(Pageable pageable, Integer userId);

    IPage<ArticleVO> getPageWithSubscribeByUserId(Pageable pageable, Integer userId);
}
