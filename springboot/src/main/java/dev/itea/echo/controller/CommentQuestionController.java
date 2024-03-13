package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.CommentQuestion;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.CommentQuestionService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import dev.itea.echo.vo.ChildCommentVO;
import dev.itea.echo.vo.CommentQuestionVO;
import dev.itea.echo.vo.RootCommentQuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问答评论控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/commentQuestion")
public class CommentQuestionController {
    @Resource
    CommentQuestionService commentQuestionService;

    /**
     * 问答评论新增
     *
     * @param commentQuestion 问答评论实体
     */
    @Operation(summary = "问答评论新增", description = "后台问答新增", tags = "CommentQuestion", method = "POST",
            parameters = {
                    @Parameter(name = "commentQuestion", description = "问答评论实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) CommentQuestion commentQuestion) {
        commentQuestionService.save(commentQuestion);
    }

    /**
     * 问答评论更新
     *
     * @param commentQuestion 问答实体
     */
    @Operation(summary = "问答评论更新", description = "问答评论更新", tags = "CommentQuestion", method = "PUT",
            parameters = {
                    @Parameter(name = "commentQuestion", description = "问答评论实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) CommentQuestion commentQuestion) {
        //check article
        CommentQuestion checkCommentQuestion = commentQuestionService.get(commentQuestion.getId());
        if (ObjectUtils.isEmpty(checkCommentQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        commentQuestionService.update(commentQuestion);
    }

    /**
     * 问答评论删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "问答删除", description = "问答评论删除", tags = "CommentQuestion", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "问答评论ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check article
        CommentQuestion checkCommentQuestion = commentQuestionService.get(id);
        if (ObjectUtils.isEmpty(checkCommentQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        commentQuestionService.delete(id);
    }

    /**
     * 问答评论查询（ID）
     *
     * @param id 问答评论ID
     * @return CommentQuestion 问答评论对象
     */
    @Operation(summary = "问答评论查询（ID）", description = "问答评论查询", tags = "CommentQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "问答评论ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public CommentQuestion get(Integer id) {
        //get article
        CommentQuestion commentQuestion = commentQuestionService.get(id);
        //check article
        if (ObjectUtils.isEmpty(commentQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return commentQuestion;
    }

    /**
     * 问答评论查询（问答评论rootId）
     *
     * @param rootId 问答评论rootId
     * @return List<ChildCommentVO> 子级问答评论值对象
     */
    @Operation(summary = "问答评论查询（rootId）", description = "问答评论次级列表查询", tags = "CommentQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "问答顶级评论ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/getChildCommentListByRootId")
    public List<ChildCommentVO> getChildListByRootId(Integer rootId) {
        return commentQuestionService.getChildListByQuestionId(rootId);
    }

    /**
     * 问答评论查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "问答评论查询（分页&关键词）", description = "后台问答评论分页与关键词查询", tags = "CommentQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAll")
    public IPage<CommentQuestionVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return commentQuestionService.getPage(pageable, pageDTO.getKeyword());
    }


    /**
     * 问答评论查询（分页&问答ID）
     *
     * @param pageDTO    分页数据传输对象
     * @param questionId 问答ID
     * @return IPage 分页对象
     */
    @Operation(summary = "问答评论查询（分页&问答ID）", description = "问答评论顶级分页查询", tags = "CommentQuestion", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "id", description = "问答ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/queryAllRootCommentByQuestionId")
    public IPage<RootCommentQuestionVO> getPageWithRootCommentByQuestionId(@Validated PageDTO pageDTO, Integer questionId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return commentQuestionService.getPageWithRootCommentByQuestionId(pageable, questionId);
    }

}
