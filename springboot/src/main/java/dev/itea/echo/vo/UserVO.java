package dev.itea.echo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户值对象
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
public class UserVO {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "个性签名")
    private String description;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @Schema(description = "最后活跃时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastActiveTime;
}