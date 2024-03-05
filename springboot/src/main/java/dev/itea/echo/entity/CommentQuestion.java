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

/**
 * 问答评论表
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("comment_question")
@Schema(description = "问答评论表")
public class CommentQuestion extends Model<CommentQuestion> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为空", groups = {UpdateValidationGroup.class})
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private Integer userId;

    @Schema(description = "问答ID")
    @TableField("question_id")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private Integer questionId;

    @Schema(description = "顶层评论ID")
    @TableField("root_comment_id")
    private Integer rootCommentId;

    @Schema(description = "关联父评论ID")
    @TableField("parent_comment_id")
    private Integer parentCommentId;

    @Schema(description = "评论内容")
    @TableField("content")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private String content;

    @Schema(description = "支持数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "否定数")
    @TableField("dislike_count")
    private Integer dislikeCount;

    @Schema(description = "发送时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
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