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

    IPage<QuestionVO> getQuestionByPage(Pageable pageable, String keyword);

    IPage<QuestionVO> getActiveQuestionByPage(Pageable pageable, String keyword);

    IPage<QuestionVO> getActiveHotArticleByPage(Pageable pageable, String keyword);

    IPage<QuestionVO> getPageByUserId(Pageable pageable, Integer userId);

    IPage<QuestionVO> getPageByCategoryId(Pageable pageable, Integer categoryId);

    IPage<QuestionVO> getPageByTagName(Pageable pageable, String tagName);

    List<Question> getArticleListByTitle(String title);

    List<UserRankVO> getUserQuestionNumRankList();

}
