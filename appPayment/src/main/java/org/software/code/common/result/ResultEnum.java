package org.software.code.common.result;

/**
 * ResultEnum 是一个枚举类，实现了 IResult 接口，用于定义通用的结果状态枚举。
 * 它将常见的操作结果（如成功和失败）进行了枚举化，每个枚举常量包含对应的状态码和消息描述，
 * 方便在系统中统一使用和管理这些结果信息。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
public enum ResultEnum implements IResult {

    // 表示操作成功的枚举常量，状态码为 200，消息为 "成功"
    SUCCESS(200, "成功"),

    // 表示操作失败的枚举常量，状态码为 400，消息为 "失败"
    FAILED(400, "失败");

    // 结果的状态码，用于标识操作的执行结果
    private Integer code;

    // 结果的消息描述，用于详细说明操作的执行情况
    private String message;

    /**
     * 无参构造函数，在枚举类中一般用于默认初始化，这里可用于一些特殊情况，但当前枚举常量初始化未使用。
     */
    ResultEnum() {
    }

    /**
     * 有参构造函数，用于初始化枚举常量的状态码和消息描述。
     *
     * @param code    结果的状态码
     * @param message 结果的消息描述
     */
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 实现 IResult 接口的方法，用于获取结果的状态码。
     *
     * @return 结果的状态码
     */
    @Override
    public Integer getCode() {
        return code;
    }

    /**
     * 设置结果的状态码。
     * 不过在枚举类中，通常枚举常量一旦定义，其属性值不应该被修改，所以此方法在实际使用中一般不常用。
     *
     * @param code 要设置的状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 实现 IResult 接口的方法，用于获取结果的消息描述。
     *
     * @return 结果的消息描述
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 设置结果的消息描述。
     * 同样，由于枚举常量的属性值一般不应被修改，此方法在实际使用中通常不常用。
     *
     * @param message 要设置的消息描述
     */
    public void setMessage(String message) {
        this.message = message;
    }
}