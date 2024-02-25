package dev.itea.echo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * ca.root_comment_id,
 *
 * @author: isixe
 * @create: 2024-02-25 15:19
 **/
@Data
@Accessors(chain = true)
public class ChildCommentArticleVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "关联父评论ID")
    private Integer parentCommentId;

    @Schema(description = "父级用户ID")
    private Integer parentUserId;

    @Schema(description = "用户名")
    private String parentUserName;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "引用评论内容")
    private String parentContent;

    @Schema(description = "支持数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "否定数")
    @TableField("dislike_count")
    private Integer dislikeCount;

    @Schema(description = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
}
