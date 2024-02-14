package dev.itea.echo.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页数据传输对象
 *
 * @author: isixe
 * @create: 2024-02-14 14:27
 **/
@Data
public class PageDTO {
    @Min(value = 0, message = "不能小于0")
    private Integer pageNum = 1;
    @Min(value = 0, message = "不能小于0")
    private Integer pageSize = 10;
    private String keyword;
}
