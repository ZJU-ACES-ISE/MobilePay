package org.software.code.common.except;

/**
 * BusinessException 是一个自定义的业务异常类，用于处理业务逻辑中的异常情况。
 * 该类继承自 RuntimeException，便于在业务处理过程中抛出和捕获特定的业务异常。
 * 通过使用该异常类，可以更好地区分系统异常和业务异常，提高异常处理的精确性。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public class BusinessException extends RuntimeException {

    // 异常的错误码，用于标识具体的异常类型
    private String code;
    
    // 异常的详细消息，用于描述异常的具体情况
    private String message;

    /**
     * 无参构造函数
     */
    public BusinessException() {
        super();
    }

    /**
     * 全参构造函数
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 通过异常枚举构造业务异常的构造函数。
     * 该构造函数接受一个 ExceptionEnum 枚举对象，从中提取错误码和错误消息来初始化异常。
     *
     * @param exceptionEnum 异常枚举对象，包含错误码和错误消息
     */
    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
    }

    /**
     * 通过异常枚举和自定义消息构造业务异常的构造函数。
     * 该构造函数接受一个 ExceptionEnum 枚举对象和自定义消息，使用枚举中的错误码和传入的自定义消息。
     *
     * @param exceptionEnum 异常枚举对象，提供错误码
     * @param customMessage 自定义的异常消息
     */
    public BusinessException(ExceptionEnum exceptionEnum, String customMessage) {
        super(customMessage);
        this.code = exceptionEnum.getCode();
        this.message = customMessage;
    }

    /**
     * 通过消息构造业务异常的构造函数。
     * 该构造函数仅接受异常消息，错误码将使用通用的运行时异常码。
     *
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ExceptionEnum.RUN_EXCEPTION.getCode();
        this.message = message;
    }

    // Getter和Setter方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}