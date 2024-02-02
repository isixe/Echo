package dev.itea.echo.controller;

import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.exception.SystemException;
import dev.itea.echo.utils.UploadFileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
//@SaCheckLogin
@RestController
@RequestMapping("/api/v1/upload")
public class FileController {

    private String uploadFilePath;

    /**
     * 文件添加
     *
     * @param files 文件
     */
    @Operation(summary = "文件上传", description = "文件上传接口", tags = "File", method = "POST",
            parameters = {
                    @Parameter(name = "File", description = "文件上传接口", required = true),
            })
    @PostMapping
    public String uploadImage(@RequestParam(value = "files") MultipartFile files){
        if (files.isEmpty() || files.getSize() <= 0) {
            throw new BusinessException(ResultCode.PARAMETER_IS_BLANK);
        }
        String path = new UploadFileUtil().uploadFile(files,"");
        if (ObjectUtils.isEmpty(path)){
            throw new SystemException(ResultCode.SYSTEM_ERROR);
        }
        return path;
    }
}
