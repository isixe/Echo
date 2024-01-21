package dev.itea.echo.exception;

import dev.itea.echo.entity.result.ResultCode;

/**
 * 自定义业务异常
 *
 * @author: isixe
 * @create: 2023-06-19 14:33
 **/
public class BusinessException extends RuntimeException {
    private ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public BusinessException(String message, ResultCode resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    public BusinessException(String message, Throwable cause, ResultCode resultCode) {
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
