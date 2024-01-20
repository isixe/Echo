package dev.itea.echo.entity.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: isixe
 * @create: 2023-06-19 09:06
 * @description: 自定义错误码
 **/
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(0, "成功"),
    FAILED(-1,"失败"),

    /*参数错误：1001-1999*/
    PARAMETER_IS_INVALID(1001, "参数无效"),
    PARAMETER_IS_BLANK(1002, "参数为空"),
    PARAMETER_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAMETER_NOT_COMPLETE(1004, "参数缺失"),
    TOKEN_IS_BLANK(1005,"token为空"),

    /*用户错误：2001-2999*/
    USER_NOT_LOGGED_IN(2001, "用户未登录，请登录后重试"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDERN(2003, "账号已被禁用"),
    USER_NOT_EXIST(2004, "用户不存在"),
    USER_HAS_EXISTED(2005, "账号已存在"),
    EMAIL_HAS_USED(2006, "邮箱已被使用"),
    TEL_HAS_USED(2007, "手机号已被使用"),

    /*权限错误：3001-3999*/
    PERMISSION_DENIED(3001, "没有访问权限"),
    TOKEN_IS_INVALID(3002,"token无效"),
    TOKEN_ERROR(3002,"token错误"),


    /*数据操作错误：4001-4999*/
    DATA_NOT_FOUND(4001, "数据未找到"),
    DATA_ALREADY_EXISTED(4002, "数据已存在"),
    DATABASE_ERROR(4003, "数据库异常"),

    /*接口调用错误：5001-5999*/
    API_NOT_FOUND(5001, "请求的接口不存在"),
    API_REQUEST_FORBIDDEN(5002, "请求的接口被禁止访问"),
    API_REQUEST_LIMIT_EXCEEDED(5003, "请求的接口访问次数超限"),

    /*系统异常：6001-6999*/
    SYSTEM_ERROR(6000, "系统异常，请稍后重试"),

    /*第三方服务错误：7001-7999*/
    EXTERNAL_SERVICE_ERROR(7000, "第三方服务异常，请稍后重试");

    private final int code;
    private final String msg;
}