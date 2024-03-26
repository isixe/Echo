package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.RecommendService;
import dev.itea.echo.vo.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 推荐控制器
 *
 * @author: isixe
 * @create: 2024-03-19 15:44
 **/
@Tag(name = "Recommend", description = "推荐接口")
@RestController
@Slf4j
@RequestMapping("/api/v1/recommend")
public class RecommendController {
    @Resource
    private RecommendService recommendService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户查询（ID）
     *
     * @param userId 用户ID
     * @return UserVO 用户值对象
     */
    @Operation(summary = "用户查询（ID）", description = "前台用户信息查询", tags = "User", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/article")
    public IPage<ArticleVO> recommendArticleByUserId(@Validated PageDTO pageDTO, Integer userId) throws IOException {
        if (ObjectUtils.isEmpty(userId)) {
            throw new BusinessException(ResultCode.PARAMETER_IS_BLANK);
        }

        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());

        IPage<ArticleVO> articleVOIPage = (IPage<ArticleVO>) redisTemplate.opsForValue().get("recommendArticle::" + userId);

        if (!ObjectUtils.isEmpty(articleVOIPage)) {
            Long expire = redisTemplate.getExpire("recommendArticle::" + userId, TimeUnit.MINUTES);
            //background update
            if (expire != null && expire < 10) {
                new Thread(() -> {
                    log.info("RecommendService Thread start...");
                    try {
                        IPage<ArticleVO> articleVOIPage1 = recommendService.seekArticleByUserId(pageable, userId);
                        redisTemplate.opsForValue().set("recommendArticle::" + userId, articleVOIPage1);
                        redisTemplate.expire("recommendArticle::" + userId, 1, TimeUnit.HOURS);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    log.info("RecommendService Thread execute end...");
                }).start();
            }

            //previous return
            return articleVOIPage;
        }

        articleVOIPage = recommendService.seekArticleByUserId(pageable, userId);
        redisTemplate.opsForValue().set("recommendArticle::" + userId, articleVOIPage);
        redisTemplate.expire("recommendArticle::" + userId, 1, TimeUnit.HOURS);
        return articleVOIPage;
    }
}
