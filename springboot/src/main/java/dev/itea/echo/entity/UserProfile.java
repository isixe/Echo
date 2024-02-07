package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 用户信息类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("user")
@Schema(description = "用户信息类")
public class UserProfile {

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户名称")
    @TableField("name")
    private String name;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "个性签名")
    @TableField("description")
    private String description;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "注册时间")
    private LocalDateTime createdTime;

    @Schema(description = "最后活跃时间")
    private LocalDateTime lastActiveTime;

}