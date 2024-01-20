package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* 文章评论表
*
* @author isixe
* @since 2024-01-15
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("comment_article")
@Schema(description = "文章评论表")
public class CommentArticle extends Model<CommentArticle> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "文章ID")
    @TableField("article_id")
    private Integer articleId;

    @Schema(description = "顶层评论ID")
    @TableField("root_comment_id")
    private Integer rootCommentId;

    @Schema(description = "关联父评论ID")
    @TableField("parent_comment_id")
    private Integer parentCommentId;

    @Schema(description = "评论内容")
    @TableField("content")
    private String content;

    @Schema(description = "发送时间")
    @TableField("created_time")
    private LocalDateTime createdTime;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}