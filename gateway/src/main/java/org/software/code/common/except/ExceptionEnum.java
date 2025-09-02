package org.software.code.common.except;

import lombok.Getter;

/**
 * 异常枚举类，定义了系统中各种业务异常的错误码和错误信息。
 * 错误码具有规范的开头，便于通过错误码快速定位对应的服务。
 * 网关服务相关的异常以 1 开头。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Getter
public enum ExceptionEnum {
    // 以下注释说明了各服务错误码的开头规则
    /*
     *
     * 错误码规范，便于通过错误码快速定位服务
     * 0、通用服务错误码统一以 0 开头
     *
     */
    // 通用服务异常
    /** 服务执行错误，提示用户稍后重试 */
    RUN_EXCEPTION("00001", "服务执行错误，请稍后重试"),
    /** Token 异常或过期，提示用户重新登录 */
    TOKEN_EXPIRED("00002", "Token异常或已过期，请重新登录"),
    /** 日期时间格式错误 */
    DATETIME_FORMAT_ERROR("00003", "日期时间格式错误"),
    /** 数据参数异常 */
    BEAN_FORMAT_ERROR("00004", "数据参数异常"),
    /** 服务通信异常，提示用户稍后重试 */
    FEIGN_EXCEPTION("00005", "服务通信异常，请稍后重试"),
    /** 未提供 Token */
    TOKEN_NOT_FIND("00006", "没有提供Token"),
    /** 请求参数异常 */
    REQUEST_PARAMETER_ERROR("00007", "请求参数异常"),
    /** 用户权限不足 */
    GATEWAY_PERMISSION_DENIED("00008", "访问权限不足"),
    ;

    // 错误码
    private String code;
    // 错误信息
    private String msg;

    /**
     * 枚举构造函数，用于初始化错误码和错误信息。
     * @param code 错误码
     * @param msg 错误信息
     */
    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}