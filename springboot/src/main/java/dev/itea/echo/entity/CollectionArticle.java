package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* 收藏表
*
* @author isixe
* @since 2024-01-15
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("collection_article")
@Schema(description = "收藏表")
public class CollectionArticle extends Model<CollectionArticle> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "收藏分组ID")
    @TableField("collection_group_id")
    private Integer collectionGroupId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "文章ID")
    @TableField("article_id")
    private Integer articleId;

    @Schema(description = "文章标题")
    @TableField("article_title")
    private String articleTitle;

    @Schema(description = "文章摘要")
    @TableField("article_desc")
    private String articleDesc;

    @Schema(description = "是否删除")
    @TableField("is_delete")
    @TableLogic
    @JsonIgnore
    private Byte isDelete;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}