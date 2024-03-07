package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.CollectionArticle;
import dev.itea.echo.entity.CommentArticle;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.CollectionArticleService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.vo.CollectionArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/collectionArticle")
public class CollectionArticleController {

    @Resource
    CollectionArticleService collectionArticleService;

    /**
     * 文章收藏新增
     *
     * @param collectionArticle 文章收藏实体
     */
    @Operation(summary = "文章收藏新增", description = "文章收藏新增", tags = "CollectionArticle", method = "POST",
            parameters = {
                    @Parameter(name = "collectionArticle", description = "文章收藏实体", required = true),
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

    /**
     * 文章收藏删除
     *
     * @param id 文章收藏ID
     */
    @Operation(summary = "文章收藏删除", description = "后台文章收藏删除", tags = "CollectionArticle", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "文章收藏ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check collectionArticle
        CollectionArticle checkCollectionArticle = collectionArticleService.get(id);
        if (ObjectUtils.isEmpty(checkCollectionArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        collectionArticleService.delete(id);
    }

    /**
     * 文章收藏查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "文章收藏查询（分页&关键词）", description = "后台文章收藏分页与关键词查询", tags = "CollectionArticle", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<CollectionArticleVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return collectionArticleService.getCollectionArticleByPage(pageable, pageDTO.getKeyword());
    }


    /**
     * 文章收藏查询（文章ID&用户ID）
     *
     * @param collectionArticle 文章收藏实体
     */
    @Operation(summary = "文章收藏查询（文章ID&用户ID）", description = "根据前台用户ID和文章ID查询文章收藏", tags = "CommentArticle", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "2"),
                    @Parameter(name = "articleId", description = "文章ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getCollect")
    public CollectionArticle getCollect(CollectionArticle collectionArticle) {
        return collectionArticleService.getOne(new LambdaQueryWrapper<CollectionArticle>()
                .eq(CollectionArticle::getUserId, collectionArticle.getUserId())
                .eq(CollectionArticle::getArticleId, collectionArticle.getArticleId()));
    }

}
