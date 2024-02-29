package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.Question;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.QuestionService;
import dev.itea.echo.utils.MapstructMapperUtil;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 问答控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {
    @Resource
    QuestionService questionService;

    /**
     * 问答新增
     *
     * @param question 问答实体
     */
    @Operation(summary = "问答新增", description = "后台问答新增", tags = "Question", method = "POST",
            parameters = {
                    @Parameter(name = "question", description = "问答实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) Question question) {
        questionService.save(question);
    }

    /**
     * 问答更新
     *
     * @param question 问答实体
     */
    @Operation(summary = "问答更新", description = "后台问答更新", tags = "Question", method = "PUT",
            parameters = {
                    @Parameter(name = "question", description = "问答实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) Question question) {
        //check question
        QuestionVO questionVO = questionService.get(question.getId());
        Question checkQuestion = MapstructMapperUtil.INSTANCE.questionVOToQuestion(questionVO);
        if (ObjectUtils.isEmpty(checkQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        questionService.update(question);
    }

    /**
     * 问答删除
     *
     * @param id 问答ID
     */
    @Operation(summary = "问答删除", description = "后台问答删除", tags = "Question", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "问答ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check question
        QuestionVO checkQuestion = questionService.get(id);
        if (ObjectUtils.isEmpty(checkQuestion)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        questionService.delete(id);
    }

    /**
     * 问答查询（ID）
     *
     * @param id 问答ID
     */
    @Operation(summary = "问答查询（ID）", description = "前台问答查询", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "问答ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public QuestionVO get(Integer id) {
        //get question
        QuestionVO questionVO = questionService.get(id);
        //check question
        if (ObjectUtils.isEmpty(questionVO)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return questionVO;
    }

    /**
     * 问答查询（分页&关键词）
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
    public IPage<QuestionVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getQuestionByPage(pageable, pageDTO.getKeyword());
    }
}
