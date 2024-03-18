package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.annotation.SaUserCheckLogin;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.CollectionQuestion;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.CollectionQuestionService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.vo.CollectionQuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 问答收藏控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/collectionQuestion")
public class CollectionQuestionController {

    @Resource
    CollectionQuestionService collectionQuestionService;

    /**
     * 问答收藏新增
     *
     * @param collectionQuestion 问答收藏实体
     */
    @Operation(summary = "问答收藏新增", description = "问答收藏新增", tags = "CollectionQuestion", method = "POST",
            parameters = {
                    @Parameter(name = "collectionQuestion", description = "问答收藏实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) CollectionQuestion collectionQuestion) {
        //check collectionQuestion
        CollectionQuestion checkCollectionQuestion = collectionQuestionService.getOne(new LambdaQueryWrapper<CollectionQuestion>()
                .eq(CollectionQuestion::getUserId, collectionQuestion.getUserId())
                .eq(CollectionQuestion::getQuestionId, collectionQuestion.getQuestionId()));
        if (!ObjectUtils.isEmpty(checkCollectionQuestion)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        collectionQuestionService.save(collectionQuestion);
    }

    /**
     * 问答收藏删除
     *
     * @param id 问答收藏ID
     */
    @Operation(summary = "问答收藏删除", description = "问答收藏删除", tags = "CollectionQuestion", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "问答收藏ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check collectionQuestion
        CollectionQuestion checkCollectionQuestion = collectionQuestionService.get(id);
        if (ObjectUtils.isEmpty(checkCollectionQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        collectionQuestionService.delete(id);
    }

    /**
     * 问答收藏查询（问答ID&用户ID）
     *
     * @param collectionArticle 问答收藏实体
     * @return CollectionArticle 问答收藏实体
     */
    @Operation(summary = "问答收藏查询（文章ID&用户ID）", description = "前台根据用户ID和文章ID查询问答收藏", tags = "CollectionQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "2"),
                    @Parameter(name = "questionId", description = "文章ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getCollect")
    public CollectionQuestion getByIdAndUserId(CollectionQuestion collectionArticle) {
        return collectionQuestionService.getOne(new LambdaQueryWrapper<CollectionQuestion>()
                .eq(CollectionQuestion::getUserId, collectionArticle.getUserId())
                .eq(CollectionQuestion::getQuestionId, collectionArticle.getQuestionId()));
    }

    /**
     * 问答收藏查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "问答收藏查询（分页&关键词）", description = "后台问答收藏分页与关键词查询", tags = "CollectionQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<CollectionQuestionVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return collectionQuestionService.getPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 问答收藏查询（分页&用户ID）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "问答收藏查询（分页&用户ID）", description = "前台根据用户ID查询问答收藏分页", tags = "CollectionQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "1")
            })
    @SaUserCheckLogin
    @GetMapping("/queryAllByUserId")
    public IPage<CollectionQuestionVO> getPageByUserId(@Validated PageDTO pageDTO, Integer userId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return collectionQuestionService.getPageByUserId(pageable, userId);
    }
}
