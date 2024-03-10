package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.UserRankVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<ArticleVO> getPage(Page<ArticleVO> page, @Param(Constants.WRAPPER) QueryWrapper<ArticleVO> wrapper);

    IPage<ArticleVO> getPageWithActive(Page<ArticleVO> page, @Param(Constants.WRAPPER) QueryWrapper<ArticleVO> wrapper);

    ArticleVO getById(Integer id);

    List<Article> getByTitle(String title);

    List<ArticleVO> getListByGroupId(Integer groupId);

    List<UserRankVO> getListWithUserNumRank();

}
