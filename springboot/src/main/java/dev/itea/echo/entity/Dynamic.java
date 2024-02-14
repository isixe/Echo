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
* 帖子表
*
* @author isixe
* @since 2024-01-15
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("dynamic")
@Schema(description = "帖子表")
public class Dynamic extends Model<Dynamic> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "帖子标题")
    @TableField("title")
    private String title;

    @Schema(description = "帖子内容")
    @TableField("content")
    private String content;

    @Schema(description = "类别ID")
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "标签")
    @TableField("tag")
    private String tag;

    @Schema(description = "发布状态")
    @TableField("status")
    private Byte status;

    @Schema(description = "浏览量")
    @TableField("pv_count")
    private Integer pvCount;

    @Schema(description = "支持数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "收藏数")
    @TableField("collection_count")
    private Integer collectionCount;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "发布时间")
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