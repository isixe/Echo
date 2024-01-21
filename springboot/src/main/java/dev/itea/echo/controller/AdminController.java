package dev.itea.echo.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Admin;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@Tag(name = "Admin", description = "管理员接口")
@RestController
@RequestMapping("/api/v0/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param name     管理员用户名
     * @param password 管理员密码
     * @return token
     */
    @Operation(summary = "管理员登录", description = "后台管理员登录", tags = "Admin", method = "POST",
            parameters = {
                    @Parameter(name = "adminName", description = "管理员用户名", required = true, example = "admin"),
                    @Parameter(name = "password", description = "管理员密码", required = true, example = "123456"),
            })
    @PostMapping("/login")
    public SaTokenInfo login(String name, String password) {
        //get data
        Admin loginAdmin = adminService.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getName, name)
                .or()
                .eq(Admin::getEmail, name));
        //check admin
        if (ObjectUtils.isEmpty(loginAdmin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //check password
        boolean flag = BCrypt.checkpw(password, loginAdmin.getPassword());
        if (!flag) {
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }
        //update status
        loginAdmin.setLastActiveTime(LocalDateTime.now());
        adminService.updateById(loginAdmin);
        //save session
        int id = loginAdmin.getId();
        StpUtil.login(id);
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
    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String token) {
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
    @Operation(summary = "管理员添加", description = "后台管理员用户新增", tags = "Admin", method = "POST",
            parameters = {
                    @Parameter(name = "admin", description = "管理员用户实体", required = true),
            })
    @PostMapping
    public void add(Admin admin) {
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
    @PutMapping
    public void update(Admin admin) {
        //check admin
        Admin checkAdmin = adminService.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getId, admin.getId()));
        if (ObjectUtils.isEmpty(checkAdmin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //encrypt
        String pwHash = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt(12));
        admin.setPassword(pwHash);
        //update
        adminService.updateById(admin);
    }

    /**
     * 管理员删除
     *
     * @param id 管理员用户ID
     */
    @Operation(summary = "管理员删除", description = "后台管理员用户删除", tags = "Admin", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "管理员用户ID", required = true),
            })
    @DeleteMapping
    public void delete(Integer id) {
        //check admin
        Admin checkAdmin = adminService.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getId, id));
        if (ObjectUtils.isEmpty(checkAdmin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //delete
        adminService.removeById(id);
    }

    /**
     * 管理员查询（ID）
     *
     * @param id 管理员用户ID
     */
    @Operation(summary = "管理员查询", description = "后台管理员用户查询", tags = "Admin", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "管理员用户ID", required = true),
            })
    @GetMapping
    public Admin getById(Integer id) {
        //get admin
        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getId, id));
        //check admin
        if (ObjectUtils.isEmpty(admin)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        return admin;
    }

    /**
     * 管理员查询（分页&关键词）
     *
     * @param pageNum  页数
     * @param pageSize 每个页的数据量
     * @param keyword  模糊搜索关键词
     * @return IPage 分页对象
     */
    @Operation(summary = "管理员分页与关键词查询", description = "后台管理员用户分页与关键词查询", tags = "Admin", method = "GET",
            parameters = {
                    @Parameter(name = "pageNum", description = "页数", required = true),
                    @Parameter(name = "pageSize", description = "每个页的数据量", required = true),
                    @Parameter(name = "keyword", description = "模糊搜索关键词", required = true),
            })
    @GetMapping("/queryAll")
    public IPage<Admin> getByName(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  String keyword) {
        if (pageNum < 0 || pageSize < 0) {
            throw new BusinessException(ResultCode.PARAMETER_IS_INVALID);
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return adminService.getAdminByPage(pageable, keyword);
    }
}
