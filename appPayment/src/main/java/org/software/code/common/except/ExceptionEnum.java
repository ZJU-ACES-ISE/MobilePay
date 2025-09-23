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

    // 支付相关异常 (5开头)
    /** 二维码格式不正确 */
    QR_CODE_FORMAT_ERROR("50001", "二维码格式不正确"),
    /** 无效的图片URL */
    INVALID_IMAGE_URL("50002", "无效的图片URL"),
    /** 参数错误 */
    PARAMETER_ERROR("50003", "参数错误"),
    /** 余额不足 */
    INSUFFICIENT_BALANCE("50004", "余额不足"),
    /** 收款码状态不允许设置金额 */
    RECEIPT_CODE_STATUS_INVALID("50005", "收款码状态不允许设置金额"),
    /** 交易状态不允许操作 */
    TRANSACTION_STATUS_INVALID("50006", "交易状态不允许操作"),
    /** 支付失败 */
    PAYMENT_FAILED("50007", "支付失败"),
    /** 收款码生成失败 */
    RECEIPT_CODE_GENERATE_FAILED("50008", "收款码生成失败"),
    /** 交通卡余额不足 */
    TRANSIT_CARD_INSUFFICIENT_BALANCE("50009", "交通卡余额不足"),
    /** 交通卡不存在 */
    TRANSIT_CARD_NOT_FOUND("50010", "交通卡不存在"),
    /** 站点信息不存在 */
    STATION_NOT_FOUND("50011", "站点信息不存在"),
    /** 票价计算失败 */
    FARE_CALCULATION_FAILED("50012", "票价计算失败"),

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