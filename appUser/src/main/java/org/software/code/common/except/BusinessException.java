package org.software.code.common.except;

/**
 * 业务异常类，继承自 RuntimeException，用于表示业务逻辑中出现的异常情况。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
public class BusinessException extends RuntimeException {

    // 异常代码
    private String code;
    // 异常消息
    private String msg;

    /**
     * 构造函数，通过代码和消息创建业务异常实例。
     * @param code 异常代码
     * @param msg 异常消息
     */
    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数，通过异常枚举创建业务异常实例。
     * @param exceptionEnum 异常枚举对象
     */
    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
    }

    /**
     * 获取异常代码。
     * @return 异常代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取异常消息。
     * @return 异常消息
     */
    public String getMsg() {
        return msg;
    }
}