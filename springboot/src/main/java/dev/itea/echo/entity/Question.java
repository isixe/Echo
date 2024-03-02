package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 问答表
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("question")
@Schema(description = "问答表")
public class Question extends Model<Question> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @NotNull(message = "不能为空", groups = UpdateValidationGroup.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户ID")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @TableField("user_id")
    private Integer userId;

    @TableField("title")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Length(message = "长度不能大于42个字符", max = 42, groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private String title;

    @Schema(description = "问答内容")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @TableField("content")
    private String content;

    @Schema(description = "类别ID")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "标签")
    @TableField("tag")
    private String tag;

    @Schema(description = "发布状态")
    @TableField("status")
    private Byte status;

    @Schema(description = "浏览数")
    @TableField("pv_count")
    private Integer pvCount;

    @Schema(description = "支持数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "收藏数")
    @TableField("collection_count")
    private Integer collectionCount;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "发布时间")
    @TableField("publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

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