package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.itea.echo.entity.ArticleThumb;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.ArticleThumbService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章点赞控制器
 *
 * @author isixe
 * @since 2024-02-27
 */
@RestController
@RequestMapping("/api/v1/articleThumb")
public class ArticleThumbController {
    @Resource
    ArticleThumbService articleThumbService;

    /**
     * 文章点赞新增
     *
     * @param articleThumb 文章点赞实体
     */
    @Operation(summary = "文章点赞新增", description = "前台文章点赞新增", tags = "ArticleThumb", method = "POST",
            parameters = {
                    @Parameter(name = "articleThumb", description = "文章点赞实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) ArticleThumb articleThumb) {
        //check articleThumb
        ArticleThumb checkArticleThumb = articleThumbService.getOne(new LambdaQueryWrapper<ArticleThumb>()
                .eq(ArticleThumb::getArticleId, articleThumb.getArticleId())
                .eq(ArticleThumb::getUserId, articleThumb.getUserId()));
        if (!ObjectUtils.isEmpty(checkArticleThumb)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        articleThumbService.save(articleThumb);
    }

    /**
     * 文章点赞删除
     *
     * @param id 文章点赞ID
     */
    @Operation(summary = "文章点赞删除", description = "前台文章点赞删除", tags = "ArticleThumb", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "文章点赞ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @DeleteMapping
    public void delete(Integer id) {
        //check articleThumb
        ArticleThumb checkArticleThumb = articleThumbService.get(id);
        if (ObjectUtils.isEmpty(checkArticleThumb)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        articleThumbService.delete(id);
    }

    /**
     * 文章点赞查询（ID）
     *
     * @param articleId 文章ID
     * @param userId    用户ID
     * @return ArticleThumb 文章点赞实体
     */
    @Operation(summary = "文章点赞查询", description = "前台文章点赞查询", tags = "ArticleThumb", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章点赞ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getByArticleIdAndUserId")
    public ArticleThumb getByArticleIdAndUserId(Integer articleId, Integer userId) {
        return articleThumbService.getOne(new LambdaQueryWrapper<ArticleThumb>()
                .eq(ArticleThumb::getArticleId, articleId)
                .eq(ArticleThumb::getUserId, userId));
    }

    /**
     * 文章点赞查询（文章ID）
     *
     * @param articleId 文章ID
     * @return ArticleThumb 文章点赞实体
     */
    @Operation(summary = "文章点赞查询", description = "前台文章点赞查询", tags = "ArticleThumb", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章点赞ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getTotalByArticle")
    public long getTotalByArticleId(Integer articleId) {
        return articleThumbService.count(new QueryWrapper<ArticleThumb>().eq("article_id", articleId));
    }

}
