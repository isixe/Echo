package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
import dev.itea.echo.vo.UserRankVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问答表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface QuestionMapper extends BaseMapper<Question> {

    QuestionVO getById(Integer id);

    IPage<QuestionVO> getPage(Page<QuestionVO> page, @Param(Constants.WRAPPER) QueryWrapper<QuestionVO> wrapper);

    IPage<QuestionVO> getPageWithActive(Page<QuestionVO> page, @Param(Constants.WRAPPER) QueryWrapper<QuestionVO> wrapper);

    List<UserRankVO> getListWithUserNumRank();

}
