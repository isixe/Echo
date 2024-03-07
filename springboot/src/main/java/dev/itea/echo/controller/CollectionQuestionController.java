package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * <p>
 * 收藏表 前端控制器
 * </p>
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
    @Operation(summary = "问答收藏新增", description = "问答收藏新增", tags = "GroupQuestion", method = "POST",
            parameters = {
                    @Parameter(name = "groupQuestion", description = "问答收藏实体", required = true),
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
    @Operation(summary = "问答收藏删除", description = "后台问答收藏删除", tags = "GroupQuestion", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "问答收藏ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check groupQuestion
        CollectionQuestion checkCollectionQuestion = collectionQuestionService.get(id);
        if (ObjectUtils.isEmpty(checkCollectionQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        collectionQuestionService.delete(id);
    }

    /**
     * 问答收藏查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "问答收藏查询（分页&关键词）", description = "后台问答收藏分页与关键词查询", tags = "GroupQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<CollectionQuestionVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return collectionQuestionService.getCollectionQuestionByPage(pageable, pageDTO.getKeyword());
    }

}
