package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.exception.SystemException;
import dev.itea.echo.utils.UploadFileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 *
 * @author: isixe
 * @create: 2024-02-01 18:43
 **/

@Tag(name = "File", description = "文件上传接口")
@SaCheckOr(
        login = {@SaCheckLogin(type = "login"), @SaCheckLogin(type = "userLogin")}
)
@RestController
@RequestMapping("/api/v1/upload")
public class FileController {

    @Resource
    private Environment environment;

    /**
     * 头像文件上传
     *
     * @param file 文件
     */
    @Operation(summary = "头像文件上传", description = "头像文件上传接口", tags = "File", method = "POST",
            parameters = {
                    @Parameter(name = "File", description = "头像文件上传接口", required = true),
            })
    @PostMapping("/avatar")
    public String uploadAvatar(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty() || file.getSize() <= 0) {
            throw new BusinessException(ResultCode.PARAMETER_IS_BLANK);
        }
        String path = new UploadFileUtil(environment).uploadFile(file, "avatar");
        if (ObjectUtils.isEmpty(path)) {
            throw new SystemException(ResultCode.SYSTEM_ERROR);
        }
        return path;
    }
}
