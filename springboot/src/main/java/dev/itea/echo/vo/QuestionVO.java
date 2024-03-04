package dev.itea.echo.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * QuestionVO
 *
 * @author: isixe
 * @create: 2024-02-29 17:18
 **/
@Data
@Accessors(chain = true)
public class QuestionVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "问答内容")
    private String content;

    @Schema(description = "类别ID")
    private Integer categoryId;

    @Schema(description = "类别")
    private String category;

    @Schema(description = "标签")
    private String tag;

    @Schema(description = "发布状态")
    private Byte status;

    @Schema(description = "浏览数")
    private Integer pvCount;

    @Schema(description = "支持数")
    private Integer likeCount;

    @Schema(description = "收藏数")
    private Integer collectionCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
}
