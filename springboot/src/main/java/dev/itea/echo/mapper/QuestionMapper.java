package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 问答表 Mapper 接口
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface QuestionMapper extends BaseMapper<Question> {

    QuestionVO getById(Integer id);

    IPage<QuestionVO> getQuestionByPage(Page<QuestionVO> page, @Param(Constants.WRAPPER)QueryWrapper<QuestionVO> wrapper);
}
