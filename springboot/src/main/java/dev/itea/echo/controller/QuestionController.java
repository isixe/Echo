package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import dev.itea.echo.vo.QuestionVO;
import dev.itea.echo.vo.UserRankVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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

    @Resource
    RedisTemplate<String, Object> redisTemplate;

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
     * @return QuestionVO 问答值对象
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

        //sync update redis question pv
        Integer pvCount = questionVO.getPvCount() + 1;
        QuestionVO articleCache = (QuestionVO) redisTemplate.opsForValue().get("question::" + id);
        Objects.requireNonNull(articleCache).setPvCount(pvCount);
        redisTemplate.opsForValue().set("question::" + id, articleCache);

        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("pv_count", pvCount);
        questionService.update(updateWrapper);

        questionVO.setPvCount(pvCount);
        return questionVO;
    }

    /**
     * 问答总数查询
     *
     * @return Integer 问答总数
     */
    @Operation(summary = "问答综述查询", description = "前台文章总数查询", tags = "Question", method = "GET")
    @SaCheckLogin
    @GetMapping("/getTotal")
    public long getTotal() {
        return questionService.count();
    }

    /**
     * 用户问答数量排行查询
     *
     * @return List<UserRankVO> 用户排行值对象列表
     */
    @Operation(summary = "用户问答数量排行查询", description = "前台用户问答数量排行查询", tags = "Question", method = "GET")
    @SaIgnore
    @GetMapping("/getUserRankList")
    public List<UserRankVO> getUserRankList() {
        return questionService.getListWithUserNumRank();
    }


    /**
     * 问答查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "文章查询（分页&关键词）", description = "后台问答分页与关键词查询", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAll")
    public IPage<QuestionVO> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 问答查询（分页&标题）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "问答查询（分页&标题）", description = "前台根据问答标题分页查询问答", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "title", description = "问答标题", required = true, example = "标题"),
            })
    @SaCheckLogin
    @GetMapping(value = "/queryAllByTitle")
    public IPage<Question> getPageByTitle(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPageWithActiveByTitle(pageable, pageDTO.getKeyword());
    }


    /**
     * 问答查询（分页&分类ID）
     *
     * @param pageDTO    分页数据传输对象
     * @param categoryId 分类ID
     * @return IPage 分页对象
     */
    @Operation(summary = "问答查询（分页&分类ID）", description = "前台根据问答分类ID分页查询问答", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "categoryId", description = "分类ID", required = true, example = "1"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllByCategoryId")
    public IPage<QuestionVO> getPageByCategoryid(@Validated PageDTO pageDTO, Integer categoryId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPageWithActiveByCategoryId(pageable, categoryId);
    }

    /**
     * 问答查询（分页&标签名称）
     *
     * @param pageDTO 分页数据传输对象
     * @param tagName 标签名称
     * @return IPage 分页对象
     */
    @Operation(summary = "问答查询（分页&标签名称）", description = "前台根据标签名称分页查询问答分页", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "tagName", description = "标签名称", required = true, example = "MySQL"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllByTagName")
    public IPage<QuestionVO> getPageByTagName(@Validated PageDTO pageDTO, String tagName) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPageWithActiveByTagName(pageable, tagName);
    }

    /**
     * 问答查询（分页&用户ID）
     *
     * @param pageDTO 分页数据传输对象
     * @param userId  用户ID
     * @return IPage 分页对象
     */
    @Operation(summary = "问答查询（分页&用户ID）", description = "前台根据用户ID分页查询问答", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "1"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllByUserId")
    public IPage<QuestionVO> getPageByUserId(@Validated PageDTO pageDTO, Integer userId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPageWithActiveByUserId(pageable, userId);
    }

    /**
     * 问答查询（分页&关键词&发布状态）
     *
     * @param pageDTO 分页数据传输对象
     * @param sort    排序方式
     * @return IPage 分页对象
     */
    @Operation(summary = "问答查询（分页&关键词&发布状态）", description = "前台问答分页、发布状态与关键词查询", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true),
                    @Parameter(name = "sort", description = "排序方式", required = true, example = "likeCount")
            })
    @SaIgnore
    @GetMapping("/queryAllActive")
    public IPage<QuestionVO> getPageWithActiveByKeyword(@Validated PageDTO pageDTO, String sort) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPageWithActive(pageable, pageDTO.getKeyword(), sort);
    }

    /**
     * 问答草稿查询（分页&用户ID）
     *
     * @param userId  用户ID
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "问答草稿查询（分页&用户ID）", description = "前台根据用户ID分页查询问答草稿", tags = "Question", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "用户ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping(value = "/queryAllDraftByUserId")
    public IPage<Question> getPageWithDraftByUserId(@Validated PageDTO pageDTO, Integer userId) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return questionService.getPageWithDraftByUserId(pageable, userId);
    }

}
