package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.LoginDTO;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.Admin;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.AdminService;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 管理员控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@Tag(name = "Admin", description = "管理员接口")
@SaCheckLogin
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param loginDTO 管理员登录传输对象
     * @return token 凭证信息
     */
    @Operation(summary = "管理员登录", description = "后台管理员登录", tags = "Admin", method = "POST",
            parameters = {
                    @Parameter(name = "loginDTO", description = "管理员登录传输对象", required = true)
            })
    @SaIgnore
    @PostMapping("/login")
    public SaTokenInfo login(@Validated LoginDTO loginDTO) {
        //get data
        Admin loginAdmin = adminService.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getName, loginDTO.getName())
                .or()
                .eq(Admin::getEmail, loginDTO.getName()));
        //check admin
        if (ObjectUtils.isEmpty(loginAdmin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //check password
        boolean flag = BCrypt.checkpw(loginDTO.getPassword(), loginAdmin.getPassword());
        if (!flag) {
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }
        //update status
        loginAdmin.setLastActiveTime(LocalDateTime.now());
        adminService.updateById(loginAdmin);
        //save session
        int id = loginAdmin.getId();
        StpUtil.login(id, loginDTO.getRememberMe());
        return StpUtil.getTokenInfo();
    }

    /**
     * 管理员退出
     *
     * @param token 管理员用户token
     */
    @Operation(summary = "管理员退出", description = "后台管理员退出", tags = "Admin", method = "POST",
            parameters = {
                    @Parameter(name = "token",
                            description = "管理员用户token",
                            required = true,
                            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiZ041S3pqTWRVWDBrQW80dXh1aDl4M2ZES0wwVHFidDEifQ.1qQAxChyCEy-kDNVKYALbEWsCfiO4ns2h0t01qZUFCk"),
            })
    @SaCheckLogin
    @PostMapping("/logout")
    public void logout(@CookieValue(value = "satoken") String token) {
        //get admin login id
        String result = (String) StpUtil.getLoginIdByToken(token);
        if (ObjectUtils.isEmpty(result)) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }
        Integer id = Integer.parseInt(result);
        //logout
        boolean flag = StpUtil.isLogin(id);
        if (!flag) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }
        StpUtil.logout(id);
    }

    /**
     * 管理员添加
     *
     * @param admin 管理员用户实体
     */
    @Operation(summary = "管理员添加", description = "后台管理员用户添加", tags = "Admin", method = "POST",
            parameters = {
                    @Parameter(name = "admin", description = "管理员用户实体", required = true),
            })
    @SaCheckLogin
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) Admin admin) {
        Admin checkAdmin = adminService.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getName, admin.getName()));
        //check admin
        if (!ObjectUtils.isEmpty(checkAdmin)) {
            throw new BusinessException(ResultCode.USER_HAS_EXISTED);
        }
        //encrypt
        String pwHash = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt(12));
        admin.setPassword(pwHash);
        //insert
        adminService.save(admin);
    }

    /**
     * 管理员更新
     *
     * @param admin 管理员用户实体
     */
    @Operation(summary = "管理员更新", description = "后台管理员用户更新", tags = "Admin", method = "PUT",
            parameters = {
                    @Parameter(name = "admin", description = "管理员用户实体", required = true),
            })
    @SaCheckLogin
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) Admin admin) {
        //check admin
        Admin checkAdmin = adminService.get(admin.getId());
        if (ObjectUtils.isEmpty(checkAdmin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //encrypt
        if (!checkAdmin.getPassword().equals(admin.getPassword())) {
            String pwHash = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt(12));
            admin.setPassword(pwHash);
        }
        //update
        adminService.update(admin);
    }

    /**
     * 管理员删除
     *
     * @param id 管理员用户ID
     */
    @Operation(summary = "管理员删除", description = "后台管理员用户删除", tags = "Admin", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "管理员用户ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @DeleteMapping
    public void delete(Integer id) {
        //check admin
        Admin checkAdmin = adminService.get(id);
        if (ObjectUtils.isEmpty(checkAdmin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //delete
        adminService.delete(id);
    }

    /**
     * 管理员查询（ID）
     *
     * @param id 管理员用户ID
     * @return Admin 管理员实体
     */
    @Operation(summary = "管理员查询（ID）", description = "后台管理员用户查询", tags = "Admin", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "管理员用户ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @GetMapping
    public Admin get(Integer id) {
        //get admin
        Admin admin = adminService.get(id);
        //check admin
        if (ObjectUtils.isEmpty(admin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //renew timeout token
        if (StpUtil.getTokenTimeout() < 86400) {
            StpUtil.renewTimeout(2592000);
        }
        return admin;
    }

    /**
     * 管理员查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "管理员查询（分页&关键词）", description = "后台管理员用户分页与关键词查询", tags = "Admin", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<Admin> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return adminService.getPage(pageable, pageDTO.getKeyword());
    }
}
