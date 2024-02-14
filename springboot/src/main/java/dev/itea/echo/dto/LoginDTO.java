package dev.itea.echo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 登录数据传输对象
 *
 * @author: isixe
 * @create: 2024-02-14 13:35
 **/
@Data
public class LoginDTO {
    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空字符串")
    @Length(message = "长度不能小于4个字符和大于16个字符", min = 4, max = 16)
    private String name;

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空字符串")
    @Length(message = "长度不能小于6个字符", min = 6)
    private String password;
    private Boolean rememberMe = false;
}
