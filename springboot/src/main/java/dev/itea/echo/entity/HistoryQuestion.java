package dev.itea.echo.entity;

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
* 用户问题浏览历史表
*
* @author isixe
* @since 2024-03-18
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("history_question")
@Schema(description = "用户问题浏览历史表")
public class HistoryQuestion extends Model<HistoryQuestion> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId("id")
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "问题ID")
    @TableField("question_id")
    private Integer questionId;

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