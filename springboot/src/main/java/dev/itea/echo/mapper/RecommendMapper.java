package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;

import java.util.List;
import java.util.Set;

/**
 * 推荐 Mapper
 *
 * @author: isixe
 * @create: 2024-03-20 18:00
 **/
public interface RecommendMapper extends BaseMapper<Object> {
    //article
    List<ArticleVO> getArticleWithCommented(Integer userId);

    List<ArticleVO> getArticleFromCollection(Integer userId);

    List<ArticleVO> getArticleWithThumb(Integer userId);

    List<ArticleVO> getArticleFromHistory(Integer userId);

    //question
    List<QuestionVO> getQuestionWithCommented(Integer userId);

    List<QuestionVO> getQuestionFromCollection(Integer userId);

    List<QuestionVO> getQuestionWithThumb(Integer userId);

    List<QuestionVO> getQuestionFromHistory(Integer userId);

    IPage<ArticleVO> getPage(Set<String> recommendArticleId);
}
