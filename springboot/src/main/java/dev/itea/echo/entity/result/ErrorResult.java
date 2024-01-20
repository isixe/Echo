package dev.itea.echo.entity.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: isixe
 * @create: 2023-06-22 15:02
 * @description: 错误响应体
 **/
@Data
@NoArgsConstructor
public class ErrorResult {
    private int code;
    private String msg;
    private final boolean status = false;

    public ErrorResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public ErrorResult(ResultCode resultCode, String message) {
        this.code = resultCode.getCode();
        this.msg = message;
    }

    public static ErrorResult fail(ResultCode resultCode) {
        return new ErrorResult(resultCode);
    }

    public static ErrorResult fail(ResultCode resultCode, String message) {
        return new ErrorResult(resultCode, message);
    }
}
