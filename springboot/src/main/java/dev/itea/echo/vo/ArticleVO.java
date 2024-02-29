package dev.itea.echo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 文章值对象
 *
 * @author: isixe
 * @create: 2024-02-14 12:03
 **/
@Data
@Accessors(chain = true)
public class ArticleVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "文章分组")
    private String articleGroupName;

    @Schema(description = "文章分组ID")
    private String articleGroupId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "作者ID")
    private String userId;

    @Schema(description = "文章头图")
    private String featuredPic;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "类别")
    private String category;

    @Schema(description = "类别ID")
    private String categoryId;

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

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}