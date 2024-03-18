package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.itea.echo.entity.HistoryQuestion;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.HistoryQuestionService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户问题浏览历史控制器
 *
 * @author isixe
 * @since 2024-03-18
 */
@RestController
@RequestMapping("/api/v1/historyQuestion")
public class HistoryQuestionController {

    @Resource
    HistoryQuestionService historyQuestionService;

    /**
     * 用户问答浏览历史新增
     *
     * @param historyQuestion 用户问答浏览历史实体
     */
    @Operation(summary = "用户问答浏览历史新增", description = "前台用户问答浏览历史新增", tags = "HistoryQuestion", method = "POST",
            parameters = {
                    @Parameter(name = "historyQuestion", description = "用户问答浏览历史实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void set(@Validated(AddValidationGroup.class) HistoryQuestion historyQuestion) {
        HistoryQuestion checkHistoryQuestion = historyQuestionService.getOne(new LambdaQueryWrapper<HistoryQuestion>()
                .eq(HistoryQuestion::getQuestionId, historyQuestion.getQuestionId())
                .eq(HistoryQuestion::getUserId, historyQuestion.getUserId()));

        if (!ObjectUtils.isEmpty(checkHistoryQuestion)) {
            checkHistoryQuestion = checkHistoryQuestion.setUpdateTime(null);
            historyQuestionService.update(checkHistoryQuestion);
        } else {
            historyQuestionService.save(historyQuestion);
        }
    }

    /**
     * 用户问答浏览历史删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "用户问答浏览历史删除", description = "前台用户问答浏览历史删除", tags = "HistoryQuestion", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "用户问答浏览历史ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check historyQuestion
        HistoryQuestion checkHistoryQuestion = historyQuestionService.get(id);
        if (ObjectUtils.isEmpty(checkHistoryQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        historyQuestionService.delete(id);
    }

    /**
     * 用户问答浏览历史查询（ID）
     *
     * @param id 用户问答浏览历史ID
     * @return HistoryQuestion 用户问答浏览历史对象
     */
    @Operation(summary = "用户问答浏览历史查询（ID）", description = "前台用户问答浏览历史查询", tags = "HistoryQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户问答浏览历史ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public HistoryQuestion get(Integer id) {
        //get historyQuestion
        HistoryQuestion historyQuestion = historyQuestionService.get(id);
        //check historyQuestion
        if (ObjectUtils.isEmpty(historyQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return historyQuestion;
    }

}
