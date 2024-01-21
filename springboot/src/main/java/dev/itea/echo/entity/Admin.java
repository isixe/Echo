package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 管理员表
 *
 * @author isixe
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("admin")
@Schema(description = "管理员表")
public class Admin extends Model<Admin> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "管理员名称")
    @TableField("name")
    private String name;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "最后活跃时间")
    @TableField(value = "last_active_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime lastActiveTime;

    @Schema(description = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime createdTime;

    @Schema(description = "是否删除")
    @TableField(value ="is_deleted",fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL)
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}