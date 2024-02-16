package dev.itea.echo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 类别值对象
 *
 * @author: isixe
 * @create: 2024-02-16 11:47
 **/
@Data
@Accessors(chain = true)
public class CategoryVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "类别名称")
    private String categoryName;
}
