package dev.itea.echo.exception;

import dev.itea.echo.entity.result.ResultCode;

/**
 * @author: isixe
 * @create: 2023-06-19 14:33
 * @description: 系统错误
 **/
public class SystemException extends RuntimeException{
    private ResultCode resultCode;

    public SystemException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public SystemException(String message, ResultCode resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    public SystemException(String message, Throwable cause, ResultCode resultCode) {
        super(message, cause);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
