package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.itea.echo.entity.CollectionArticle;
import dev.itea.echo.entity.Follow;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.FollowService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户关注控制器
 *
 * @author isixe
 * @since 2024-03-13
 */
@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    @Resource
    FollowService followService;

    /**
     * 用户关注新增
     *
     * @param follow 用户关注实体
     */
    @Operation(summary = "用户关注新增", description = "后台用户关注新增", tags = "Follow", method = "POST",
            parameters = {
                    @Parameter(name = "follow", description = "用户关注实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) Follow follow) {
        followService.save(follow);
    }

    /**
     * 用户关注删除
     *
     * @param id 评论ID
     */
    @Operation(summary = "用户关注删除", description = "用户关注删除", tags = "Follow", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "用户关注ID", required = true, example = "2"),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @DeleteMapping
    public void delete(Integer id) {
        //check follow
        Follow checkFollow = followService.get(id);
        if (ObjectUtils.isEmpty(checkFollow)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        followService.delete(id);
    }

    /**
     * 用户关注查询（ID）
     *
     * @param id 用户关注ID
     * @return Follow 用户关注对象
     */
    @Operation(summary = "用户关注查询（ID）", description = "用户关注查询", tags = "Follow", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户关注ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public Follow get(Integer id) {
        //get follow
        Follow follow = followService.get(id);
        //check follow
        if (ObjectUtils.isEmpty(follow)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return follow;
    }

    /**
     * 用户关注查询（用户ID&关注用户ID）
     *
     * @param userId       用户关注ID
     * @param followUserId 关注用户ID
     * @return Follow 用户关注对象
     */
    @Operation(summary = "用户关注查询（ID）", description = "用户关注查询", tags = "Follow", method = "GET",
            parameters = {
                    @Parameter(name = "userId", description = "用户关注ID", required = true, example = "2"),
                    @Parameter(name = "followUserId", description = "关注用户ID", required = true, example = "3"),
            })
    @SaIgnore
    @GetMapping("/getByUserIdAndFollowId")
    public Follow getByUserIdAndFollowUserId(Integer userId, Integer followUserId) {
        return followService.getOne(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, followUserId));
    }
}
