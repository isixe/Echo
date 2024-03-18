package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.itea.echo.entity.QuestionThumb;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.QuestionThumbService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 问答点赞控制器
 *
 * @author isixe
 * @since 2024-02-27
 */
@RestController
@RequestMapping("/api/v1/questionThumb")
public class QuestionThumbController {

    @Resource
    QuestionThumbService questionThumbService;

    /**
     * 问答点赞新增
     *
     * @param questionThumb 问答点赞实体
     */
    @Operation(summary = "问答点赞新增", description = "前台问答点赞新增", tags = "QuestionThumb", method = "POST",
            parameters = {
                    @Parameter(name = "questionThumb", description = "问答点赞实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) QuestionThumb questionThumb) {
        //check questionThumb
        QuestionThumb checkQuestionThumb = questionThumbService.getOne(new LambdaQueryWrapper<QuestionThumb>()
                .eq(QuestionThumb::getQuestionId, questionThumb.getQuestionId())
                .eq(QuestionThumb::getUserId, questionThumb.getUserId()));
        if (!ObjectUtils.isEmpty(checkQuestionThumb)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        questionThumbService.save(questionThumb);
    }

    /**
     * 问答点赞删除
     *
     * @param id 问答点赞ID
     */
    @Operation(summary = "问答点赞删除", description = "前台问答点赞删除", tags = "QuestionThumb", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "问答点赞ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @DeleteMapping
    public void delete(Integer id) {
        //check questionThumb
        QuestionThumb checkQuestionThumb = questionThumbService.get(id);
        if (ObjectUtils.isEmpty(checkQuestionThumb)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        questionThumbService.delete(id);
    }

    /**
     * 问答点赞查询（ID）
     *
     * @param questionId 问答ID
     * @param userId     用户ID
     * @return QuestionThumb 问答点赞实体
     */
    @Operation(summary = "问答点赞查询", description = "前台问答点赞查询", tags = "QuestionThumb", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "问答点赞ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getByQuestionIdAndUserId")
    public QuestionThumb getByArticleIdAndUserId(Integer questionId, Integer userId) {
        return questionThumbService.getOne(new LambdaQueryWrapper<QuestionThumb>()
                .eq(QuestionThumb::getQuestionId, questionId)
                .eq(QuestionThumb::getUserId, userId));
    }
}
