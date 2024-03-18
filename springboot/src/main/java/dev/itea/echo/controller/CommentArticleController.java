package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.CommentArticle;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.CommentArticleService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import dev.itea.echo.vo.ChildCommentVO;
import dev.itea.echo.vo.CommentArticleVO;
import dev.itea.echo.vo.RootCommentArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章评论控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/commentArticle")
public class CommentArticleController {

    @Resource
    CommentArticleService commentArticleService;

    /**
     * 文章评论新增
     *
     * @param commentArticle 文章评论实体
     */
    @Operation(summary = "文章评论新增", description = "文章评论新增", tags = "CommentArticle", method = "POST",
            parameters = {
                    @Parameter(name = "commentArticle", description = "文章评论实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) CommentArticle commentArticle) {
        commentArticleService.save(commentArticle);
    }

    /**
     * 文章评论更新
     *
     * @param commentArticle 文章实体
     */
    @Operation(summary = "文章评论更新", description = "文章评论更新", tags = "CommentArticle", method = "PUT",
            parameters = {
                    @Parameter(name = "commentArticle", description = "文章评论实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) CommentArticle commentArticle) {
        //check article
        CommentArticle checkCommentArticle = commentArticleService.get(commentArticle.getId());
        if (ObjectUtils.isEmpty(checkCommentArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        commentArticleService.update(commentArticle);
    }

    /**
     * 文章评论删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "文章评论删除", description = "文章评论删除", tags = "CommentArticle", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "文章评论ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check article
        CommentArticle checkCommentArticle = commentArticleService.get(id);
        if (ObjectUtils.isEmpty(checkCommentArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        commentArticleService.delete(id);
    }

    /**
     * 文章评论查询（ID）
     *
     * @param id 文章评论ID
     * @return CommentArticle 文章评论对象
     */
    @Operation(summary = "文章评论查询（ID）", description = "前台文章评论查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章评论ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public CommentArticle get(Integer id) {
        //get article
        CommentArticle commentArticle = commentArticleService.get(id);
        //check article
        if (ObjectUtils.isEmpty(commentArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return commentArticle;
    }

    /**
     * 文章评论查询（文章评论rootId）
     *
     * @param rootId 文章评论rootId
     * @return List<ChildCommentVO> 子级文章评论值对象列表
     */
    @Operation(summary = "文章评论查询（rootId）", description = "前台文章评论次级列表查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章顶级评论ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getChildCommentListByRootId")
    public List<ChildCommentVO> getChildListByRootId(Integer rootId) {
        return commentArticleService.getChildListByArticleId(rootId);
    }

    /**
     * 文章评论查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "文章评论查询（分页&关键词）", description = "后台文章评论分页与关键词查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAll")
    public IPage<CommentArticleVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return commentArticleService.getPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 文章评论查询（文章ID）
     *
     * @param pageDTO   分页数据传输对象
     * @param articleId 文章ID
     * @return IPage 分页对象
     */
    @Operation(summary = "文章评论查询（文章ID）", description = "前台文章评论顶级列表查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/queryAllRootCommentByArticleId")
    public IPage<RootCommentArticleVO> getPageWithRootCommentByArticleId(@Validated PageDTO pageDTO, Integer articleId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return commentArticleService.getPageWithRootCommentByArticleId(pageable, articleId);
    }

}
