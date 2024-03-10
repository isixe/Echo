package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CommentQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问答评论表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CommentQuestionMapper extends BaseMapper<CommentQuestion> {

    IPage<CommentQuestionVO> getPage(Page<RootCommentQuestionVO> page, @Param(Constants.WRAPPER) QueryWrapper<RootCommentQuestionVO> wrapper);

    List<RootCommentQuestionVO> getRootListByQuestionId(@Param(Constants.WRAPPER) QueryWrapper<RootCommentQuestionVO> wrapper);

    List<ChildCommentVO> getChildListByQuestionId(@Param(Constants.WRAPPER) QueryWrapper<ChildCommentVO> wrapper);

}
