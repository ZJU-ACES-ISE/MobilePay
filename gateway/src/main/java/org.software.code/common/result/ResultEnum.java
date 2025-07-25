package org.software.code.common.result;

/**
 * ResultEnum 枚举类定义了系统中常用的结果状态码和对应的消息。
 * 该枚举实现了 IResult 接口，提供了统一的成功和失败状态标识。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public enum ResultEnum implements IResult {

    /** 操作成功 */
    SUCCESS(200, "操作成功"),
    
    /** 操作失败 */
    FAILED(500, "操作失败");

    // 状态码
    private final Integer code;
    // 消息描述  
    private final String message;

    /**
     * 枚举构造函数，用于初始化状态码和消息描述。
     * 
     * @param code 状态码
     * @param message 消息描述
     */
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}