package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.annotation.SaUserCheckLogin;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.Article;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.ArticleService;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.UserRankVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章前端控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    ArticleService articleService;

    /**
     * 文章新增
     *
     * @param article 文章实体
     */
    @Operation(summary = "文章新增", description = "后台文章新增", tags = "Article", method = "POST",
            parameters = {
                    @Parameter(name = "article", description = "文章实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) Article article) {
        articleService.save(article);
    }

    /**
     * 文章更新
     *
     * @param article 文章实体
     */
    @Operation(summary = "文章更新", description = "后台文章更新", tags = "Article", method = "PUT",
            parameters = {
                    @Parameter(name = "article", description = "文章实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) Article article) {
        //check article
        Article checkArticle = articleService.getOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, article.getId()));
        if (ObjectUtils.isEmpty(checkArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        articleService.updateById(article);
    }

    /**
     * 文章删除
     *
     * @param id 文章ID
     */
    @Operation(summary = "文章删除", description = "后台文章删除", tags = "Article", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "文章ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check article
        Article checkArticle = articleService.getOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, id));
        if (ObjectUtils.isEmpty(checkArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        articleService.removeById(id);
    }

    /**
     * 文章查询（ID）
     *
     * @param id 文章ID
     */
    @Operation(summary = "文章查询（ID）", description = "后台文章查询", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章ID", required = true, example = "2"),
            })
    @SaUserCheckLogin
    @GetMapping
    public Article getById(Integer id) {
        //get article
        Article article = articleService.getOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, id));
        //check article
        if (ObjectUtils.isEmpty(article)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return article;
    }


    /**
     * 文章查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "文章查询（分页&关键词）", description = "后台文章分页与关键词查询", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAll")
    public IPage<ArticleVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getArticleByPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 用户文章数量排行查询
     */
    @Operation(summary = "用户文章数量排行查询", description = "前台用户文章数量排行查询", tags = "Article", method = "GET")
    @SaIgnore
    @GetMapping("/userRank")
    public List<UserRankVO> getByUserRankList() {
        return articleService.getUserArticleNumRankList();
    }
}
