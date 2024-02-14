package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import dev.itea.echo.service.ArticleService;
import dev.itea.echo.vo.UserRankVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 用户文章数量排行查询
     */
    @Operation(summary = "用户文章数量排行查询", description = "前台用户文章数量排行查询", tags = "Article", method = "GET")
    @SaIgnore
    @GetMapping("/userRank")
    public List<UserRankVO> getByUserRankList() {
        return articleService.getUserArticleNumRankList();
    }
}
