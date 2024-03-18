package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.itea.echo.entity.ReactionCommentArticle;
import dev.itea.echo.entity.ReactionCommentQuestion;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.ReactionCommentQuestionService;
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
 * 问答评论反应控制器
 *
 * @author isixe
 * @since 2024-03-16
 */
@RestController
@RequestMapping("/api/v1/reactionCommentQuestion")
public class ReactionCommentQuestionController {

    @Resource
    ReactionCommentQuestionService reactionCommentQuestionService;

    /**
     * 问答评论反应新增
     *
     * @param reactionCommentQuestion 问答评论反应实体
     */
    @Operation(summary = "问答评论反应新增", description = "前台问答评论反应新增", tags = "CommentArticle", method = "POST",
            parameters = {
                    @Parameter(name = "reactionCommentQuestion", description = "问答评论反应实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) ReactionCommentQuestion reactionCommentQuestion) {
        reactionCommentQuestionService.save(reactionCommentQuestion);
    }

    /**
     * 问答评论反应更新
     *
     * @param reactionCommentQuestion 问答评论反应实体
     */
    @Operation(summary = "问答评论反应更新", description = "前台问答评论反应更新", tags = "CommentArticle", method = "PUT",
            parameters = {
                    @Parameter(name = "reactionCommentQuestion", description = "问答评论反应实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) ReactionCommentQuestion reactionCommentQuestion) {
        //check article
        ReactionCommentQuestion checkReaction = reactionCommentQuestionService.get(reactionCommentQuestion.getId());
        if (ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        reactionCommentQuestionService.update(reactionCommentQuestion);
    }

    /**
     * 问答评论反应删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "问答评论反应删除", description = "前台问答评论反应删除", tags = "CommentArticle", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "问答评论反应ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check article
        ReactionCommentQuestion checkReaction = reactionCommentQuestionService.get(id);
        if (ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        reactionCommentQuestionService.delete(id);
    }

    /**
     * 问答评论反应查询（ID）
     *
     * @param id 问答评论反应ID
     * @return CommentArticle 问答评论反应对象
     */
    @Operation(summary = "问答评论反应查询（ID）", description = "前台问答评论反应查询", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "问答评论反应ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public ReactionCommentQuestion get(Integer id) {
        //get article
        ReactionCommentQuestion checkReaction = reactionCommentQuestionService.get(id);
        //check article
        if (ObjectUtils.isEmpty(checkReaction)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return checkReaction;
    }

    /**
     * 问答评论反应查询（用户ID&问答评论ID）
     *
     * @param userId            用户ID
     * @param commentQuestionId 问答评论ID
     * @return CommentArticle 问答评论反应对象
     */
    @Operation(summary = "问答评论反应查询（ID）", description = "前台根据用户ID和问答评论ID查询问答评论反应", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "问答评论反应ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getByCommentQuestionIdAndUserId")
    public ReactionCommentQuestion getByCommentArticleIdAndUserId(Integer userId, Integer commentQuestionId) {
        return reactionCommentQuestionService.getOne(new QueryWrapper<ReactionCommentQuestion>()
                .eq("user_id", userId)
                .eq("comment_question_id", commentQuestionId));
    }
}
