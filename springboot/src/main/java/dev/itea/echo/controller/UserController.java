package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.annotation.SaUserCheckLogin;
import dev.itea.echo.dto.LoginDTO;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.dto.RegisterDTO;
import dev.itea.echo.entity.User;
import dev.itea.echo.utils.MapstructMapperUtil;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.UserService;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import dev.itea.echo.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户前端控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@Tag(name = "User", description = "用户接口")
@RestController
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginDTO 管理员登录传输对象
     * @return token 凭证信息
     */
    @Operation(summary = "用户登录", description = "前台用户登录", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "loginDTO", description = "管理员登录传输对象", required = true)
            })
    @SaIgnore
    @PostMapping("/login")
    public SaTokenInfo login(@Validated LoginDTO loginDTO) {
        //get data
        User loginUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, loginDTO.getName())
                .or()
                .eq(User::getEmail, loginDTO.getName()));
        //check user
        if (ObjectUtils.isEmpty(loginUser)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //check password
        boolean flag = BCrypt.checkpw(loginDTO.getPassword(), loginUser.getPassword());
        if (!flag) {
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }
        //update status
        loginUser.setLastActiveTime(LocalDateTime.now());
        userService.updateById(loginUser);
        //save session
        int id = loginUser.getId();
        StpUserUtil.login(id, loginDTO.getRememberMe());
        return StpUserUtil.getTokenInfo();
    }

    /**
     * 用户退出
     *
     * @param token 用户token
     */
    @Operation(summary = "用户退出", description = "前台用户退出", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "token",
                            description = "用户token",
                            required = true,
                            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiZ041S3pqTWRVWDBrQW80dXh1aDl4M2ZES0wwVHFidDEifQ.1qQAxChyCEy-kDNVKYALbEWsCfiO4ns2h0t01qZUFCk"),
            })
    @SaUserCheckLogin
    @PostMapping("/logout")
    public void logout(@CookieValue(value = "satokenuser") String token) {
        //get user login id
        String result = (String) StpUserUtil.getLoginIdByToken(token);
        if (ObjectUtils.isEmpty(result)) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }
        Integer id = Integer.parseInt(result);
        //logout
        boolean flag = StpUserUtil.isLogin(id);
        if (!flag) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }
        StpUserUtil.logout(id);
    }

    /**
     * 用户注册
     *
     * @param registerDTO 用户注册数据传输对象
     */
    @Operation(summary = "用户注册", description = "前台用户注册", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "name", description = "用户名", required = true, example = "user"),
                    @Parameter(name = "password", description = "密码", required = true, example = "123456"),
                    @Parameter(name = "email", description = "邮箱", required = true, example = "email@example.com"),
            })
    @SaIgnore
    @PostMapping("/register")
    public void register(@Validated RegisterDTO registerDTO) {
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, registerDTO.getName()).or().eq(User::getEmail, registerDTO.getEmail()));
        //check user
        if (!ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_HAS_EXISTED);
        }
        //encrypt
        String pwHash = BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt(12));
        registerDTO.setPassword(pwHash);
        //insert
        User user = MapstructMapperUtil.INSTANCE.registerDTOToUser(registerDTO);
        userService.save(user);
    }

    /**
     * 用户新增
     *
     * @param user 用户实体
     */
    @Operation(summary = "用户新增", description = "后台用户新增", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "user", description = "用户实体", required = true),
            })
    @SaCheckLogin
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) User user) {
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, user.getName()));
        //check user
        if (!ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_HAS_EXISTED);
        }
        //encrypt
        String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(pwHash);
        //insert
        userService.save(user);
    }

    /**
     * 用户更新
     *
     * @param user 用户实体
     */
    @Operation(summary = "用户更新", description = "后台用户更新", tags = "User", method = "PUT",
            parameters = {
                    @Parameter(name = "user", description = "用户实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) User user) {
        //check user
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, user.getId()));
        if (ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //encrypt
        if (!checkUser.getPassword().equals(user.getPassword())) {
            String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(pwHash);
        }
        //update
        userService.updateById(user);
    }

    /**
     * 用户删除
     *
     * @param id 用户ID
     */
    @Operation(summary = "用户删除", description = "后台用户删除", tags = "User", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @DeleteMapping
    public void delete(Integer id) {
        //check user
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id));
        if (ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //delete
        userService.removeById(id);
    }

    /**
     * 用户信息查询
     *
     * @param id 用户ID
     */
    @Operation(summary = "用户信息查询", description = "前台用户信息查询", tags = "User", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public UserVO getById(Integer id) {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id));
        UserVO userVO = MapstructMapperUtil.INSTANCE.userToUserVO(user);
        if (ObjectUtils.isEmpty(userVO)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        return userVO;
    }


    /**
     * 用户查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "用户分页与关键词查询", description = "后台用户分页与关键词查询", tags = "User", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<User> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return userService.getUserByPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 用户模糊查询
     *
     * @param name 用户名称
     */
    @GetMapping("/queryByName")
    public List<UserVO> getByName(String name) {
        return userService.getUserListByName(name);
    }
}
