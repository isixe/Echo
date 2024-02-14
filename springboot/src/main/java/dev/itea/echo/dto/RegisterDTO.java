package dev.itea.echo.dto;

import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册数据传输对象
 *
 * @author: isixe
 * @create: 2024-02-14 15:03
 **/
@Data
public class RegisterDTO {

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空字符串")
    @Length(message = "长度不能小于4个字符和大于16个字符")
    private String name;

    @NotNull(message = "不能为空")
    @NotBlank(message = "不能为空字符串")
    @Length(message = "长度不能小于6个字符")
    private String password;

    @NotNull(message = "不能为空")
    @Email(message = "邮箱格式错误")
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "非法邮箱格式")
    private String email;
}
