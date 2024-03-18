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
* 用户文章浏览历史表
*
* @author isixe
* @since 2024-03-18
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("history_article")
@Schema(description = "用户文章浏览历史表")
public class HistoryArticle extends Model<HistoryArticle> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "文章ID")
    @TableField("article_id")
    private Integer articleId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "浏览时间")
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