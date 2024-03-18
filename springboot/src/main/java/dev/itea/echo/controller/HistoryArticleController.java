package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.itea.echo.entity.HistoryArticle;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.HistoryArticleService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;

/**
 * 用户文章浏览历史控制器
 *
 * @author isixe
 * @since 2024-03-18
 */
@RestController
@RequestMapping("/api/v1/historyArticle")
public class HistoryArticleController {

    @Resource
    HistoryArticleService historyArticleService;

    /**
     * 用户文章浏览历史新增
     *
     * @param historyArticle 用户文章浏览历史实体
     */
    @Operation(summary = "用户文章浏览历史新增", description = "后台用户文章浏览历史新增", tags = "HistoryArticle", method = "POST",
            parameters = {
                    @Parameter(name = "historyArticle", description = "用户文章浏览历史实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void set(@Validated(AddValidationGroup.class) HistoryArticle historyArticle) {
        HistoryArticle checkHistoryArticle = historyArticleService.getOne(new LambdaQueryWrapper<HistoryArticle>()
                .eq(HistoryArticle::getArticleId, historyArticle.getArticleId())
                .eq(HistoryArticle::getUserId, historyArticle.getUserId()));

        if (!ObjectUtils.isEmpty(checkHistoryArticle)) {
            checkHistoryArticle = checkHistoryArticle.setUpdateTime(null);
            historyArticleService.update(checkHistoryArticle);
        } else {
            historyArticleService.save(historyArticle);
        }
    }

    /**
     * 用户文章浏览历史删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "用户文章浏览历史删除", description = "用户文章浏览历史删除", tags = "HistoryArticle", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "用户文章浏览历史ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check historyArticle
        HistoryArticle checkHistoryArticle = historyArticleService.get(id);
        if (ObjectUtils.isEmpty(checkHistoryArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        historyArticleService.delete(id);
    }

    /**
     * 用户文章浏览历史查询（ID）
     *
     * @param id 用户文章浏览历史ID
     * @return HistoryArticle 用户文章浏览历史对象
     */
    @Operation(summary = "用户文章浏览历史查询（ID）", description = "用户文章浏览历史查询", tags = "HistoryArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户文章浏览历史ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public HistoryArticle get(Integer id) {
        //get historyArticle
        HistoryArticle historyArticle = historyArticleService.get(id);
        //check historyArticle
        if (ObjectUtils.isEmpty(historyArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return historyArticle;
    }

}
