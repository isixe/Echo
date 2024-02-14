package dev.itea.echo.mapper;

import dev.itea.echo.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.UserRankVO;

import java.util.List;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface ArticleMapper extends BaseMapper<Article> {
    List<UserRankVO> getUserArticleNumRankList();
}
