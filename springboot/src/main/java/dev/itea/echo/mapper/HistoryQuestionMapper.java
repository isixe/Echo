package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.HistoryQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.HistoryQuestionVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户问题浏览历史表 Mapper 接口
 * </p>
 *
 * @author isixe
 * @since 2024-03-18
 */
public interface HistoryQuestionMapper extends BaseMapper<HistoryQuestion> {

    IPage<HistoryQuestionVO> getPageByUserId(Page<HistoryQuestionVO> page, @Param(Constants.WRAPPER) QueryWrapper<HistoryQuestionVO> wrapper);
}
