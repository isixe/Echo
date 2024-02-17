package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.annotation.SaUserCheckLogin;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.*;
import dev.itea.echo.entity.GroupArticle;
import dev.itea.echo.entity.GroupArticle;
import dev.itea.echo.entity.GroupArticle;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.GroupArticleService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文章分组控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/groupArticle")
public class GroupArticleController {

    @Resource
    private GroupArticleService groupArticleService;

    /**
     * 文章分组新增
     *
     * @param groupArticle 文章分组实体
     */
    @Operation(summary = "文章分组新增", description = "后台文章分组新增", tags = "GroupArticle", method = "POST",
            parameters = {
                    @Parameter(name = "groupArticle", description = "文章分组实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) GroupArticle groupArticle) {
        //check groupArticle
        GroupArticle checkGroupArticle = groupArticleService.getOne(new LambdaQueryWrapper<GroupArticle>()
                .eq(GroupArticle::getName, groupArticle.getName()));
        if (!ObjectUtils.isEmpty(checkGroupArticle)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        groupArticleService.save(groupArticle);
    }

    /**
     * 文章分组更新
     *
     * @param groupArticle 文章分组实体
     */
    @Operation(summary = "文章分组更新", description = "后台文章分组更新", tags = "GroupArticle", method = "PUT",
            parameters = {
                    @Parameter(name = "groupArticle", description = "文章分组实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) GroupArticle groupArticle) {
        //check groupArticle
        GroupArticle checkGroupArticle = groupArticleService.getOne(new LambdaQueryWrapper<GroupArticle>()
                .eq(GroupArticle::getId, groupArticle.getId()));
        if (ObjectUtils.isEmpty(checkGroupArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        groupArticleService.updateById(groupArticle);
    }

    /**
     * 文章分组删除
     *
     * @param id 文章分组ID
     */
    @Operation(summary = "文章分组删除", description = "后台文章分组删除", tags = "GroupArticle", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "文章分组ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check groupArticle
        GroupArticle checkGroupArticle = groupArticleService.getOne(new LambdaQueryWrapper<GroupArticle>()
                .eq(GroupArticle::getId, id));
        if (ObjectUtils.isEmpty(checkGroupArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        groupArticleService.removeById(id);
    }

    /**
     * 文章分组查询（ID）
     *
     * @param id 文章分组ID
     */
    @Operation(summary = "文章分组查询（ID）", description = "后台文章分组查询", tags = "GroupArticle", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "文章分组ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public GroupArticle getById(Integer id) {
        //get groupArticle
        GroupArticle groupArticle = groupArticleService.getOne(new LambdaQueryWrapper<GroupArticle>()
                .eq(GroupArticle::getId, id));
        //check groupArticle
        if (ObjectUtils.isEmpty(groupArticle)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return groupArticle;
    }

    /**
     * 类别查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "类别查询（分页&关键词）", description = "后台类别分页与关键词查询", tags = "GroupArticle", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<GroupArticle> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return groupArticleService.getGroupArticleByPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 文章分组查询（UserID）
     *
     * @param userId 用户ID
     */
    @Operation(summary = "文章分组查询（UserID）", description = "后台文章分组用户ID查询", tags = "GroupArticle", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "文章分组所属用户ID", required = true, example = "1"),
            })
    @SaIgnore
    @GetMapping("getByUserId")
    public List<Map<String, Object>> getByUserId(Integer userId) {
        return groupArticleService.getGroupArticleByUserId(userId);
    }

    /**
     * 文章分组查询（UserID&GroupName）
     *
     * @param userId 用户ID
     */
    @Operation(summary = "文章分组查询（UserID&GroupName）", description = "后台文章分组用户ID和分组名称查询", tags = "GroupArticle", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "文章分组所属用户ID", required = true, example = "1"),
                    @Parameter(name = "groupName", description = "文章分组名称", required = true, example = "分组名"),
            })
    @SaIgnore
    @GetMapping("getByUserIdAndGroupName")
    public GroupArticle getByUserIdAndGroupName(Integer userId, String groupName) {
        return groupArticleService.getOne(new QueryWrapper<GroupArticle>()
                .eq("user_id", userId)
                .eq("name", groupName));
    }
}
