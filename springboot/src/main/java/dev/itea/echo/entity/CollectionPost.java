package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.io.Serial;
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
@TableName("collection_post")
@Schema(description = "收藏表")
public class CollectionPost extends Model<CollectionPost> {
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

    @Schema(description = "帖子ID")
    @TableField("post_id")
    private Integer postId;

    @Schema(description = "帖子标题")
    @TableField("post_title")
    private String postTitle;

    @Schema(description = "帖子摘要")
    @TableField("post_desc")
    private String postDesc;

    @Schema(description = "是否删除")
    @TableField("is_delete")
    private Byte isDelete;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}