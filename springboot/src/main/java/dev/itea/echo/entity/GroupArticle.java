package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章分组表
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("group_article")
@Schema(description = "文章分组表")
public class GroupArticle extends Model<GroupArticle> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "文章分组名称")
    @TableField("name")
    private String name;

    @Schema(description = "分组描述")
    @TableField("description")
    private String description;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    @TableLogic
    @JsonIgnore
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}