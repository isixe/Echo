package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.HistoryArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.HistoryArticleVO;
import org.apache.ibatis.annotations.Param;

/**
 * 用户文章浏览历史表 Mapper 接口
 *
 * @author isixe
 * @since 2024-03-18
 */
public interface HistoryArticleMapper extends BaseMapper<HistoryArticle> {

    IPage<HistoryArticleVO> getPageByUserId(Page<HistoryArticleVO> page, @Param(Constants.WRAPPER) QueryWrapper<HistoryArticleVO> wrapper);
}
