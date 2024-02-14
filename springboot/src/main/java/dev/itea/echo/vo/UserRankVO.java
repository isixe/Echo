package dev.itea.echo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户排行类
 *
 * @author: isixe
 * @create: 2024-02-13 11:55
 **/

@Data
@Accessors(chain = true)
public class UserRankVO {
    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "个性签名")
    private String description;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "排行数量")
    private Integer rankSum;
}
