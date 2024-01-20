package dev.itea.echo.entity.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: isixe
 * @create: 2023-06-19 09:06
 * @description: 成功响应体
 **/
@Data
@NoArgsConstructor
public class Result<T> {
    private int code;
    private T data;
    private String msg;
    private final boolean status = true;;

    public Result(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.data = data;
        this.msg = ResultCode.SUCCESS.getMsg();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.data = data;
        this.msg = resultCode.getMsg();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }
}
