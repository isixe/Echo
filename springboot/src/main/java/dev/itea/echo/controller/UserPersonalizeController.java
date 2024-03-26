package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.itea.echo.annotation.SaUserCheckLogin;
import dev.itea.echo.entity.UserPersonalize;
import dev.itea.echo.entity.UserPersonalize;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.UserPersonalizeService;
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
 * 用户个性化设置控制器
 *
 * @author isixe
 * @since 2024-03-26
 */
@RestController
@RequestMapping("/api/v1/personalize")
public class UserPersonalizeController {

    @Resource
    UserPersonalizeService userPersonalizeService;

    /**
     * 用户个性化设置新增
     *
     * @param userPersonalize 用户个性化设置实体
     */
    @Operation(summary = "用户个性化设置新增", description = "用户个性化设置新增", tags = "UserPersonalize", method = "POST",
            parameters = {
                    @Parameter(name = "userPersonalize", description = "用户个性化设置实体", required = true),
            })
    @SaUserCheckLogin
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) UserPersonalize userPersonalize) {
        //check userPersonalize
        UserPersonalize checkPersonalize = userPersonalizeService.getOne(new LambdaQueryWrapper<UserPersonalize>()
                .eq(UserPersonalize::getUserId, userPersonalize.getUserId()));
        if (!ObjectUtils.isEmpty(checkPersonalize)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        userPersonalizeService.save(userPersonalize);
    }

    /**
     * 用户个性化设置更新
     *
     * @param userPersonalize 用户个性化设置实体
     */
    @Operation(summary = "用户个性化设置更新", description = "用户个性化设置更新", tags = "UserPersonalize", method = "PUT",
            parameters = {
                    @Parameter(name = "userPersonalizeService", description = "用户个性化设置实体", required = true),
            })
    @SaUserCheckLogin
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) UserPersonalize userPersonalize) {
        //check userPersonalizeService
        UserPersonalize checkUserPersonalize = userPersonalizeService.get(userPersonalize.getUserId());
        if (ObjectUtils.isEmpty(checkUserPersonalize)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        userPersonalizeService.update(userPersonalize);
    }

    /**
     * 用户个性化设置查询（ID）
     *
     * @param id 用户个性化设置ID
     * @return UserPersonalize 用户个性化设置实体
     */
    @Operation(summary = "用户个性化设置查询", description = "前台用户个性化设置查询", tags = "UserPersonalize", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户个性化设置ID", required = true, example = "2"),
            })
    @SaUserCheckLogin
    @GetMapping
    public UserPersonalize get(Integer id) {
        //get userPersonalizeService
        UserPersonalize userPersonalize = userPersonalizeService.get(id);
        //check userPersonalizeService
        if (ObjectUtils.isEmpty(userPersonalizeService)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return userPersonalize;
    }

}
