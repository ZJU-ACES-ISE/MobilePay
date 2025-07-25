package org.software.code.common.result;

/**
 * IResult 接口定义了结果对象应具备的基本方法。
 * 通过实现此接口，可以确保不同的结果类型具有统一的访问方式，
 * 便于在系统中进行统一的结果处理和错误管理。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface IResult {

    /**
     * 获取结果的状态码。
     * 状态码用于标识操作的执行结果，如成功、失败等。
     *
     * @return 结果的状态码
     */
    Integer getCode();

    /**
     * 获取结果的消息描述。
     * 消息用于详细说明操作的执行情况，如成功提示、失败原因等。
     *
     * @return 结果的消息描述
     */
    String getMessage();
}