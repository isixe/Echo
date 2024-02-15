package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 文章表
 *
 * @author isixe
 * @since 2024-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("article")
@Schema(description = "文章表")
public class Article extends Model<Article> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @NotNull(message = "不能为空", groups = UpdateValidationGroup.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "文章分组ID")
    @TableField("article_group_id")
    private Integer articleGroupId;

    @Schema(description = "用户ID")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "文章头图")
    @TableField("featured_pic")
    private String featuredPic;

    @Schema(description = "文章标题")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Length(message = "长度不能大于42个字符", max = 16, groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @TableField("title")
    private String title;

    @Schema(description = "摘要")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Length(message = "长度不能大于200个字符", max = 200, groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @TableField("summary")
    private String summary;

    @Schema(description = "文章内容")
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
    @TableField("collecion_count")
    private Integer collecionCount;

    @Schema(description = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime createdTime;

    @Schema(description = "发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}