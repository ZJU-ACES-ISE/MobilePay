package org.software.code.common.constants;

/**
 * 折扣策略相关常量
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
public class DiscountConstants {
    
    /**
     * 折扣策略类型
     */
    public static class StrategyType {
        public static final String TRAVEL = "TRAVEL";      // 出行
        public static final String PAYMENT = "PAYMENT";    // 支付
        public static final String NEW_USER = "NEW_USER";  // 新用户
        public static final String HOLIDAY = "HOLIDAY";    // 节假日
    }
    
    /**
     * 折扣类型
     */
    public static class DiscountType {
        public static final String PERCENTAGE = "PERCENTAGE";      // 百分比
        public static final String FIXED_AMOUNT = "FIXED_AMOUNT";  // 固定金额
        public static final String LADDER = "LADDER";              // 阶梯
    }
    
    /**
     * 策略状态
     */
    public static class Status {
        public static final String ACTIVE = "ACTIVE";      // 激活
        public static final String INACTIVE = "INACTIVE";  // 停用
        public static final String EXPIRED = "EXPIRED";    // 过期
    }
} 