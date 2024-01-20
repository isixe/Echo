package dev.itea.echo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.itea.echo.entity.Admin;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
@Tag(name="Admin",description = "管理员接口")
@RestController
@RequestMapping("/api/v0/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param admin 管理员实体
     */
    @Operation(summary = "管理员登录", description = "后台管理员登录", tags = "Admin",method = "POST")
    @PostMapping("/login")
    public void login(Admin admin) {
        //TODO 登录和加密
    }

    @PostMapping
    public void add(Admin admin) {

    }

    @PutMapping
    public void update(Integer id) {

    }

    @DeleteMapping
    public void delete(Integer id) {

    }

    @GetMapping
    public void getById(Integer id) {

    }
}
