package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.itea.echo.entity.ReactionCommentArticle;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.ReactionCommentArticleService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章评论反应反应控制器
 *
 * @author isixe
 * @since 2024-03-16
 */
@RestController
@RequestMapping("/api/v1/reactionCommentArticle")
public class ReactionCommentArticleController {

    @Resource
    ReactionCommentArticleService reactionCommentArticleService;

    /**
     * 文章评论反应新增
     *
     * @param reactionCommentArticle 文章评论反应实体
     */
    @Operation(summary = "文章评论反应新增", description = "前台文章评论反应新增", tags = "CommentArticle", method = "POST",
            parameters = {
                    @Parameter(name = "reactionCommentArticle", description = "文章评论反应实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) ReactionCommentArticle reactionCommentArticle) {
        ReactionCommentArticle checkReaction = reactionCommentArticleService.getOne(new LambdaQueryWrapper<ReactionCommentArticle>()
                .eq(ReactionCommentArticle::getUserId, reactionCommentArticle.getUserId())
                .eq(ReactionCommentArticle::getCommentArticleId, reactionCommentArticle.getCommentArticleId()));
        if (!ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        reactionCommentArticleService.save(reactionCommentArticle);
    }

    /**
     * 文章评论反应更新
     *
     * @param reactionCommentArticle 文章实体
     */
    @Operation(summary = "文章评论反应更新", description = "前台文章评论反应更新", tags = "CommentArticle", method = "PUT",
            parameters = {
                    @Parameter(name = "reactionCommentArticle", description = "文章评论反应实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) ReactionCommentArticle reactionCommentArticle) {
        //check article
        ReactionCommentArticle checkReaction = reactionCommentArticleService.get(reactionCommentArticle.getId());
        if (ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        reactionCommentArticleService.update(reactionCommentArticle);
    }

    /**
     * 文章评论反应删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "文章删除", description = "前台文章评论反应删除", tags = "CommentArticle", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "文章评论反应ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check article
        ReactionCommentArticle checkReaction = reactionCommentArticleService.get(id);
        if (ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        reactionCommentArticleService.delete(id);
    }

    /**
     * 文章评论反应查询（ID）
     *
     * @param id 文章评论反应ID
     * @return CommentArticle 文章评论反应对象
     */
    @Operation(summary = "文章评论反应查询（ID）", description = "前台文章评论反应查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章评论反应ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public ReactionCommentArticle get(Integer id) {
        //get article
        ReactionCommentArticle checkReaction = reactionCommentArticleService.get(id);
        //check article
        if (ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return checkReaction;
    }

    /**
     * 文章评论反应查询（uID）
     *
     * @param userId           用户ID
     * @param commentArticleId 文章评论ID
     * @return CommentArticle 文章评论反应对象
     */
    @Operation(summary = "文章评论反应查询（ID）", description = "前台文章评论反应查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章评论反应ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getByCommentArticleIdAndUserId")
    public ReactionCommentArticle getByCommentArticleIdAndUserId(Integer userId, Integer commentArticleId) {
        return reactionCommentArticleService.getOne(new QueryWrapper<ReactionCommentArticle>()
                .eq("user_id", userId)
                .eq("comment_article_id", commentArticleId));
    }

}
