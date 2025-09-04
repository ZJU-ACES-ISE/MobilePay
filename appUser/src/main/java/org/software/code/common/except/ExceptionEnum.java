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

    // 用户认证相关异常 (2开头)
    /** 用户不存在 */
    USER_NOT_FOUND("20001", "用户不存在"),
    /** 用户账户已冻结 */
    USER_ACCOUNT_FROZEN("20002", "用户账户已冻结"),
    /** 密码错误 */
    PASSWORD_ERROR("20003", "密码错误"),
    /** 用户身份验证失败 */
    USER_IDENTITY_INVALID("20004", "用户身份验证失败"),
    /** 用户注册失败 */
    USER_REGISTER_FAILED("20005", "用户注册失败"),
    /** 用户登录失败 */
    USER_LOGIN_FAILED("20006", "用户登录失败"),
    /** 用户信息修改失败 */
    USER_UPDATE_FAILED("20007", "用户信息修改失败"),
    /** 头像上传失败 */
    AVATAR_UPLOAD_FAILED("20008", "头像上传失败"),
    /** 用户审核信息上传失败 */
    USER_AUDIT_UPLOAD_FAILED("20009", "用户审核信息上传失败"),
    /** 用户审核查询失败 */
    USER_AUDIT_QUERY_FAILED("20010", "用户审核查询失败"),
    /** 文件格式不支持 */
    FILE_FORMAT_NOT_SUPPORTED("20011", "文件格式不支持"),
    /** 缺少必填参数 */
    REQUIRED_PARAMETER_MISSING("20012", "缺少必填参数"),

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