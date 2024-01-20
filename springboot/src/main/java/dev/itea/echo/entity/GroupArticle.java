package dev.itea.echo.entity;

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
* 文章分组表
*
* @author isixe
* @since 2024-01-15
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("group_article")
@Schema(description = "文章分组表")
public class GroupArticle extends Model<GroupArticle> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId("id")
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "文章分组名称")
    @TableField("name")
    private String name;

    @Schema(description = "分组描述")
    @TableField("description")
    private String description;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}