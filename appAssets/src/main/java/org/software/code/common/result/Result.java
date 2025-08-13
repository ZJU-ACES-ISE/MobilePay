package org.software.code.common.result;

import lombok.Data;

/**
 * Result 是一个通用的结果封装类，使用泛型 T 来支持不同类型的数据返回。
 * 该类用于统一封装服务层处理业务逻辑后返回的结果，包含状态码、消息和具体数据，
 * 方便前端或其他调用方统一处理和解析服务端的响应。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Data
public class Result<T> {

    // 结果的状态码，用于标识操作的执行结果，如成功、失败等
    private Integer code;
    // 结果的消息描述，用于详细说明操作的执行情况，如成功提示、失败原因等
    private String message;
    // 结果中携带的具体数据，数据类型由泛型 T 决定
    private T data;

    /**
     * 静态方法，用于创建一个表示操作成功且包含具体数据的 Result 对象。
     * 使用预定义的成功状态码和消息，同时将传入的数据封装到 Result 对象中。
     *
     * @param data 操作成功后返回的具体数据，类型为泛型 T
     * @param <T>  泛型类型
     * @return 包含成功状态码、默认成功消息和具体数据的 Result 对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 静态方法，用于创建一个表示操作成功且包含自定义消息和具体数据的 Result 对象。
     * 使用预定义的成功状态码，同时将传入的自定义消息和数据封装到 Result 对象中。
     *
     * @param message 自定义的成功消息
     * @param data    操作成功后返回的具体数据，类型为泛型 T
     * @param <T>     泛型类型
     * @return 包含成功状态码、自定义成功消息和具体数据的 Result 对象
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 静态方法，用于创建一个表示操作失败的 Result 对象。
     * 使用预定义的失败状态码和消息，不携带具体数据。
     *
     * @return 包含失败状态码和默认失败消息的 Result 对象
     */
    public static Result<?> failed() {
        return new Result<>(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMessage(), null);
    }

    /**
     * 静态方法，用于创建一个表示操作成功但不携带具体数据的 Result 对象。
     * 使用预定义的成功状态码和消息，不携带具体数据。
     *
     * @return 包含成功状态码和默认成功消息的 Result 对象
     */
    public static Result<?> success() {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 静态方法，用于创建一个表示操作失败且包含自定义消息的 Result 对象。
     * 使用预定义的失败状态码，同时将传入的自定义消息封装到 Result 对象中，不携带具体数据。
     *
     * @param message 自定义的失败消息
     * @return 包含失败状态码和自定义失败消息的 Result 对象
     */
    public static Result<?> failed(String message) {
        return new Result<>(ResultEnum.FAILED.getCode(), message, null);
    }

    /**
     * 静态方法，用于创建一个表示操作失败且使用 IResult 接口实现类提供的状态码和消息的 Result 对象。
     * 不携带具体数据，适用于根据实现了 IResult 接口的错误结果对象来创建失败结果。
     *
     * @param errorResult 实现了 IResult 接口的错误结果对象，提供状态码和消息
     * @return 包含从 errorResult 获取的状态码和消息的 Result 对象
     */
    public static Result<?> failed(IResult errorResult) {
        return new Result<>(errorResult.getCode(), errorResult.getMessage(), null);
    }

    /**
     * 无参构造函数，用于创建一个空的 Result 对象。
     */
    public Result() {
    }

    /**
     * 有参构造函数，用于创建一个包含指定状态码、消息和数据的 Result 对象。
     *
     * @param code    结果的状态码
     * @param message 结果的消息描述
     * @param data    结果中携带的具体数据，类型为泛型 T
     */
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 静态方法，用于创建一个包含指定状态码、消息和数据的 Result 对象。
     * 先创建一个空的 Result 对象，再分别设置状态码、消息和数据，最后返回该对象。
     *
     * @param code    结果的状态码
     * @param message 结果的消息描述
     * @param data    结果中携带的具体数据，类型为泛型 T
     * @param <T>     泛型类型
     * @return 包含指定状态码、消息和数据的 Result 对象
     */
    public static <T> Result<T> instance(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}