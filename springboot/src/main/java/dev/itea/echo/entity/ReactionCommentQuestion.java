package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* 问答评论反应表
*
* @author isixe
* @since 2024-03-16
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("reaction_comment_question")
@Schema(description = "问答评论反应表")
public class ReactionCommentQuestion extends Model<ReactionCommentQuestion> {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("comment_article_id")
    private Integer commentArticleId;

    @TableField("status")
    private Byte status;

    @TableField("is_deleted")
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}