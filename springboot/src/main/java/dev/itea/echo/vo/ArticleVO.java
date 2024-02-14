package dev.itea.echo.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章传输实体类
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
    private String articleGroupId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "副标题")
    private String subTitle;

    @Schema(description = "文章内容")
    private String content;

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
    private Integer collecionCount;

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