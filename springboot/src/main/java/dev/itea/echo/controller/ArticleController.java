package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.Article;
import dev.itea.echo.utils.MapstructMapperUtil;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 文章控制器
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

    @Resource
    RedisTemplate<String, Object> redisTemplate;

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
        ArticleVO articleVO = articleService.get(article.getId());
        Article checkArticle = MapstructMapperUtil.INSTANCE.articleVOToArticle(articleVO);
        if (ObjectUtils.isEmpty(checkArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        articleService.update(article);
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
        ArticleVO checkArticle = articleService.get(id);
        if (ObjectUtils.isEmpty(checkArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        articleService.delete(id);
    }

    /**
     * 文章查询（ID）
     *
     * @param id 文章ID
     * @return ArticleVO 文章值对象
     */
    @Operation(summary = "文章查询（ID）", description = "前台文章查询", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public ArticleVO get(Integer id) {
        //get article
        ArticleVO articleVO = articleService.get(id);
        //check article
        if (ObjectUtils.isEmpty(articleVO)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        //sync update redis article pv
        Integer pvCount = articleVO.getPvCount() + 1;
        ArticleVO articleCache = (ArticleVO) redisTemplate.opsForValue().get("article::" + id);
        Objects.requireNonNull(articleCache).setPvCount(pvCount);
        redisTemplate.opsForValue().set("article::" + id, articleCache);

        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("pv_count", pvCount);
        articleService.update(updateWrapper);

        articleVO.setPvCount(pvCount);
        return articleVO;
    }

    /**
     * 文章分组更新
     *
     * @param id      文章ID
     * @param groupId 分组ID
     */
    @Operation(summary = "文章分组更新", description = "文章分组更新", tags = "Article", method = "PUT",
            parameters = {
                    @Parameter(name = "id", description = "文章ID", required = true),
                    @Parameter(name = "groupId", description = "文章分组ID", required = true),
            })
    @SaCheckLogin(type = StpUserUtil.TYPE)
    @PutMapping("/group")
    public void updateGroupId(Integer id, Integer groupId) {
        //check article
        ArticleVO articleVO = articleService.get(id);
        Article checkArticle = MapstructMapperUtil.INSTANCE.articleVOToArticle(articleVO);
        if (ObjectUtils.isEmpty(checkArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        articleService.updateArticleGroupId(id, groupId);
    }

    /**
     * 文章分组更新
     *
     * @param id 文章ID
     */
    @Operation(summary = "文章分组移除", description = "文章分组移除", tags = "Article", method = "PUT",
            parameters = {
                    @Parameter(name = "id", description = "文章ID", required = true),
            })
    @SaCheckLogin(type = StpUserUtil.TYPE)
    @DeleteMapping("/group")
    public void deleteGroupId(Integer id) {
        //check article
        ArticleVO articleVO = articleService.get(id);
        Article checkArticle = MapstructMapperUtil.INSTANCE.articleVOToArticle(articleVO);
        if (ObjectUtils.isEmpty(checkArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        articleService.deleteArticleGroupId(id);
    }

    /**
     * 文章查询（分组ID）
     *
     * @param groupId 文章分组ID
     * @return List<ArticleVO> 文章值对象列表
     */
    @Operation(summary = "文章查询（分组ID）", description = "前台根据文章分组ID查询文章", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "groupId", description = "文章分组ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping(value = "/getList", params = "groupId")
    public List<ArticleVO> getListByGroupId(Integer groupId) {
        return articleService.getListByGroupId(groupId);
    }

    /**
     * 文章草稿查询（用户ID）
     *
     * @param userId 用户ID
     * @return List<Article> 文章对象列表
     */
    @Operation(summary = "文章查询（用户ID）", description = "前台根据用户ID查询文章", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping(value = "/getDraftList")
    public List<Article> getDraftListByUserId(Integer userId) {
        return articleService.getListWithDraftByUserId(userId);
    }

    /**
     * 用户文章数量排行查询
     *
     * @return List<UserRankVO> 用户排行列表
     */
    @Operation(summary = "用户文章数量排行查询", description = "前台用户文章数量排行查询", tags = "Article", method = "GET")
    @SaIgnore
    @GetMapping("/getUserRankList")
    public List<UserRankVO> getUserRankList() {
        return articleService.getListWithUserNumRank();
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
        return articleService.getPage(pageable, pageDTO.getKeyword());
    }


    /**
     * 文章查询（分页&文章标题）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "文章查询（分页&文章标题）", description = "前台文章标题文章分页查询", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "title", description = "文章标题", required = true, example = "标题"),
            })
    @SaCheckLogin
    @GetMapping(value = "/queryAllByTitle")
    public IPage<Article> getPageWithActiveByByTitle(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithActiveByTitle(pageable, pageDTO.getKeyword());
    }


    /**
     * 已发布文章查询（分页&关键词&发布状态）
     *
     * @param pageDTO 分页数据传输对象
     * @param sort    排序字段
     * @return IPage 分页对象
     */
    @Operation(summary = "已发布文章查询（分页&关键词&发布状态）", description = "前台已发布文章分页与关键词、发布状态查询", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAllActive")
    public IPage<ArticleVO> getPageWithActiveByKeyword(@Validated PageDTO pageDTO, String sort) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithActive(pageable, pageDTO.getKeyword(), sort);
    }

    /**
     * 已发布热门文章查询（分页&关键词&发布状态）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "已发布热门文章查询（分页&关键词&发布状态）", description = "前台已发布热门文章分页、发布状态与关键词查询", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAllHotActive")
    public IPage<ArticleVO> getPageWithHotActiveByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithHotActive(pageable, pageDTO.getKeyword());
    }

    /**
     * 文章查询（分页&分类ID）
     *
     * @param pageDTO    分页数据传输对象
     * @param categoryId 分类ID
     * @return IPage 分页对象
     */
    @Operation(summary = "文章查询（分页&分类ID）", description = "前台根据文章分类ID查询文章", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "categoryId", description = "分类ID", required = true, example = "1"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllByCategoryId")
    public IPage<ArticleVO> getPageWithActiveByCategoryid(@Validated PageDTO pageDTO, Integer categoryId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithActiveByCategoryId(pageable, categoryId);
    }

    /**
     * 文章查询（分页&用户ID）
     *
     * @param pageDTO 分页数据传输对象
     * @param userId  用户ID
     * @return IPage 分页对象
     */
    @Operation(summary = "文章查询（分页&用户ID）", description = "前台根据用户ID查询文章", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "1"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllByUserId")
    public IPage<ArticleVO> getPageWithActiveByUserId(@Validated PageDTO pageDTO, Integer userId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithActiveByUserId(pageable, userId);
    }

    /**
     * 未分组文章查询（分页&用户ID）
     *
     * @param pageDTO 分页数据传输对象
     * @param userId  用户ID
     * @return IPage 分页对象
     */
    @Operation(summary = "未分组文章列表查询（用户ID）", description = "前台根据用户ID查询未分组文章", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "1"),
            })
    @SaIgnore
    @GetMapping(value = "/queryUnGroupByUserId")
    public IPage<ArticleVO> getPageWithActiveAndUnGroupByUserId(@Validated PageDTO pageDTO, Integer userId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithActiveAndUnGroupByUserId(pageable, userId);
    }

    /**
     * 文章查询（分页&标签名称）
     *
     * @param pageDTO 分页数据传输对象
     * @param tagName 标签名称
     * @return IPage 分页对象
     */
    @Operation(summary = "文章分页模糊查询（标签名称）", description = "前台根据标签名称查询文章分页", tags = "Article", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "tagName", description = "标签名称", required = true, example = "MySQL"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllByTagName")
    public IPage<ArticleVO> getPageWithActiveByTagName(@Validated PageDTO pageDTO, String tagName) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return articleService.getPageWithActiveByTagName(pageable, tagName);
    }

}
