package dev.itea.echo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 问答评论值对象
 *
 * @author: isixe
 * @create: 2024-03-05 10:14
 **/
@Data
@Accessors(chain = true)
public class CommentQuestionVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "问答ID")
    private Integer questionId;

    @Schema(description = "问答标题")
    private String questionTitle;

    @Schema(description = "顶层评论ID")
    private Integer rootCommentId;

    @Schema(description = "关联父评论ID")
    private Integer parentCommentId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "支持数")
    private Integer likeCount;

    @Schema(description = "否定数")
    private Integer dislikeCount;

    @Schema(description = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

}
