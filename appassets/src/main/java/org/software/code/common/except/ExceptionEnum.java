package org.software.code.common.except;

import lombok.Getter;

/**
 * 异常枚举类，定义了系统中各种业务异常的错误码和错误信息。
 * 错误码具有规范的开头，便于通过错误码快速定位对应的服务。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Getter
public enum ExceptionEnum {
    // 以下注释说明了各服务错误码的开头规则
    /**
     * 错误码规范，便于通过错误码快速定位服务
     * 0、通用服务错误码统一以 0 开头
     * 1、gateway   服务错误码统一以 1 开头
     * 2、user   服务错误码统一以 2 开头
     * 3、health - code    服务错误码统一以 3 开头
     * 4、itinerary - code  服务错误码统一以 4 开头
     * 5、nucleic - acids  服务错误码统一以 5 开头
     * 6、place - code  服务错误码统一以 6 开头
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

    // 场所码相关异常
    /** 场所码添加失败 */
    PLACE_CODE_INSERT_FAIL("60001", "场所码添加失败"),
    /** 场所码更新失败 */
    PLACE_CODE_UPDATE_FAIL("60002", "场所码更新失败"),
    /** 场所码删除失败 */
    PLACE_CODE_DELETE_FAIL("60003", "场所码删除失败"),
    /** 场所码查询失败 */
    PLACE_CODE_SELECT_FAIL("60004", "场所码查询失败"),

    // 用户场所码记录相关异常
    /** 用户场所码记录添加失败 */
    USER_PLACE_CODE_INSERT_FAIL("60005", "用户场所码记录添加失败"),
    /** 用户场所码记录更新失败 */
    USER_PLACE_CODE_UPDATE_FAIL("60006", "用户场所码记录更新失败"),
    /** 用户场所码记录删除失败 */
    USER_PLACE_CODE_DELETE_FAIL("60007", "用户场所码记录删除失败"),
    /** 用户场所码记录查询失败 */
    USER_PLACE_CODE_SELECT_FAIL("60008", "用户场所码记录查询失败"),

    /** 场所码不存在 */
    PLACE_CODE_NOT_FIND("60009", "场所码不存在"),

    // 用户认证相关异常
    /** 用户不存在 */
    USER_NOT_FOUND("20001", "用户不存在"),
    /** 用户账户已冻结 */
    USER_ACCOUNT_FROZEN("20002", "用户账户已冻结"),
    /** 密码错误 */
    PASSWORD_ERROR("20003", "密码错误"),
    /** 用户身份验证失败 */
    USER_IDENTITY_INVALID("20004", "用户身份验证失败"),

    // 管理员认证相关异常
    /** 管理员不存在 */
    ADMIN_NOT_FOUND("30001", "管理员不存在"),
    /** 管理员账户已锁定 */
    ADMIN_ACCOUNT_LOCKED("30002", "管理员账户已锁定"),
    /** 管理员密码错误 */
    ADMIN_PASSWORD_ERROR("30003", "管理员密码错误"),
    /** 管理员权限不足 */
    ADMIN_PERMISSION_DENIED("30004", "管理员权限不足"),
    /** 管理员账户未激活 */
    ADMIN_ACCOUNT_INACTIVE("30005", "管理员账户未激活"),

    // 折扣策略相关异常
    /** 折扣策略不存在 */
    STRATEGY_NOT_FOUND("40001", "折扣策略不存在"),
    /** 折扣策略已过期 */
    STRATEGY_EXPIRED("40002", "折扣策略已过期"),
    /** 折扣策略已禁用 */
    STRATEGY_DISABLED("40003", "折扣策略已禁用"),
    /** 折扣策略使用次数已达上限 */
    STRATEGY_USAGE_LIMIT_REACHED("40004", "折扣策略使用次数已达上限"),
    /** 折扣策略规则验证失败 */
    STRATEGY_VALIDATION_FAILED("40005", "折扣策略规则验证失败"),
    /** 未达到折扣策略最低消费金额 */
    STRATEGY_MIN_AMOUNT_NOT_MET("40006", "未达到折扣策略最低消费金额"),
    /** 折扣策略不适用于当前用户 */
    STRATEGY_NOT_APPLICABLE("40007", "折扣策略不适用于当前用户"),

    //银行卡相关异常
    CARD_ALREADY_BOUND("50001", "当前银行卡已被绑定"),
    VERIFY_CODE_EXPIRED("50002", "验证码已过期"),
    VERIFY_CODE_INVALID("50003", "验证码错误"),
    DATA_NOT_FOUND("50004", "银行卡不存在"),

    //资产相关异常
    PAY_PASSWORD_INVALID("60001", "支付密码错误"),
    BALANCE_NOT_ENOUGH("60002", "用户余额不足"),
    BALANCE_NOT_CREATE("60003", "用户余额表未创建" );

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