package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CommentArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.ChildCommentVO;
import dev.itea.echo.vo.CommentArticleVO;
import dev.itea.echo.vo.RootCommentArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章评论表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CommentArticleMapper extends BaseMapper<CommentArticle> {

    IPage<CommentArticleVO> getArticleCommentByPage(Page<CommentArticleVO> page, @Param(Constants.WRAPPER) QueryWrapper<CommentArticleVO> wrapper);

    List<RootCommentArticleVO> getRootListByArticleId(@Param(Constants.WRAPPER) QueryWrapper<RootCommentArticleVO> wrapper);

    List<ChildCommentVO> getChildListByArticleId(@Param(Constants.WRAPPER) QueryWrapper<ChildCommentVO> wrapper);
}
