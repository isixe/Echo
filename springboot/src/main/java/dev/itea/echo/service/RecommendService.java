package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

/**
 * 推荐 服务类
 *
 * @author: isixe
 * @create: 2024-03-19 15:45
 **/
public interface RecommendService {

    void seekArticleToRedis();

    void seekQuesionToRedis();

    IPage<ArticleVO> seekArticleByUserId(Pageable pageable, Integer userId) throws IOException;

    IPage<QuestionVO> seekQuestionByUserId(Pageable pageable, Integer userId) throws IOException;
}
