package dev.itea.echo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 问答浏览记录值对象
 *
 * @author: isixe
 * @create: 2024-03-18 19:20
 **/
@Data
@Accessors(chain = true)
public class HistoryQuestionVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "问答ID")
    private Integer questionId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "作者ID")
    private String userId;

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

}
