package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.itea.echo.entity.CollectionArticle;
import dev.itea.echo.entity.GroupArticle;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.CollectionArticleService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/collection-article")
public class CollectionArticleController {

    @Resource
    CollectionArticleService collectionArticleService;

    /**
     * 文章收藏新增
     *
     * @param collectionArticle 文章收藏实体
     */
    @Operation(summary = "文章收藏新增", description = "文章收藏新增", tags = "GroupArticle", method = "POST",
            parameters = {
                    @Parameter(name = "groupArticle", description = "文章收藏实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) CollectionArticle collectionArticle) {
        //check collectionArticle
        CollectionArticle checkCollectionArticle = collectionArticleService.getOne(new LambdaQueryWrapper<CollectionArticle>()
                .eq(CollectionArticle::getUserId, collectionArticle.getUserId())
                .eq(CollectionArticle::getArticleId, collectionArticle.getArticleId()));
        if (!ObjectUtils.isEmpty(checkCollectionArticle)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        collectionArticleService.save(collectionArticle);
    }

}
