package org.software.code.common.except;

import lombok.Getter;

/**
 * 异常枚举类，定义了系统中各种业务异常的错误码和错误信息。
 * 错误码具有规范的开头，便于通过错误码快速定位对应的服务。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Getter
public enum ExceptionEnum {
    // 以下注释说明了各服务错误码的开头规则
    /**
     * 错误码规范，便于通过错误码快速定位服务
     * 0、通用服务错误码统一以 0 开头
     * 1、gateway   服务错误码统一以 1 开头
     * 2、user   服务错误码统一以 2 开头
     * 3、admin    服务错误码统一以 3 开头
     * 4、assets  服务错误码统一以 4 开头
     * 5、payment  服务错误码统一以 5 开头
     * 6、ai  服务错误码统一以 6 开头
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

    // 资产相关异常 (4开头)
    /** 数据不存在 */
    DATA_NOT_FOUND("40001", "数据不存在"),
    /** 支付密码错误 */
    PAY_PASSWORD_INVALID("40002", "支付密码错误"),
    /** 用户余额不足 */
    BALANCE_NOT_ENOUGH("40003", "用户余额不足"),
    /** 用户余额表未创建 */
    BALANCE_NOT_CREATE("40004", "用户余额表未创建"),
    /** 余额不足 */
    BALANCE_INSUFFICIENT("40005", "余额不足"),
    /** 银行卡已被绑定 */
    CARD_ALREADY_BOUND("40006", "当前银行卡已被绑定"),
    /** 验证码已过期 */
    VERIFY_CODE_EXPIRED("40007", "验证码已过期"),
    /** 验证码错误 */
    VERIFY_CODE_INVALID("40008", "验证码错误"),
    /** 银行卡不存在 */
    BANK_CARD_NOT_FOUND("40009", "银行卡不存在"),
    /** 账单更新失败 */
    UPDATE_FAILED("40010", "账单更新失败"),
    /** 银行转账失败 */
    BANK_TRANSFER_FAILED("40011", "银行转账失败"),
    /** 请求参数不合法 */
    INVALID_REQUEST_PARAMETER("40012", "请求参数不合法"),

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