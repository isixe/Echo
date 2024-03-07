package dev.itea.echo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 问答收藏值对象
 *
 * @author: isixe
 * @create: 2024-03-06 21:24
 **/
@Data
@Accessors(chain = true)
public class CollectionQuestionVO {
    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名")
    private String author;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "问答ID")
    private Integer questionId;

    @Schema(description = "问答标题")
    private String title;

    @Schema(description = "类别")
    private String category;

    @Schema(description = "类别ID")
    private String categoryId;

    @Schema(description = "浏览数")
    private Integer pvCount;

    @Schema(description = "支持数")
    private Integer likeCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
