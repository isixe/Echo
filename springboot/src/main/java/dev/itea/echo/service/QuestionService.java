package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
import dev.itea.echo.vo.UserRankVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 问答表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface QuestionService extends IService<Question> {

    QuestionVO get(Integer id);

    Question update(Question question);

    void delete(Integer id);

    List<UserRankVO> getListWithUserNumRank();

    IPage<QuestionVO> getPage(Pageable pageable, String keyword);

    IPage<QuestionVO> getPageWithActive(Pageable pageable, String keyword, String sort);

    IPage<QuestionVO> getPageWithActiveByUserId(Pageable pageable, Integer userId);

    IPage<QuestionVO> getPageWithActiveByCategoryId(Pageable pageable, Integer categoryId);

    IPage<QuestionVO> getPageWithActiveByTagName(Pageable pageable, String tagName);

    IPage<Question> getPageWithActiveByTitle(Pageable pageable, String title);

    IPage<Question> getPageWithDraftByUserId(Pageable pageable, Integer userId);

    IPage<QuestionVO> getPageWithSubscribeByUserId(Pageable pageable, Integer userId);
}
