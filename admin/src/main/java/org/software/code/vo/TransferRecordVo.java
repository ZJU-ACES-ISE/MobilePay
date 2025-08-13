package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 转账记录视图对象
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRecordVo {


    /**
     * 转账编号
     */
    private String transferNumber;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 转账类型：1-充值，2-提现，3-转账
     */
    private Integer type;

    /**
     * 转账类型名称
     */
    private String typeName;

    /**
     * 银行卡ID
     */
    private Long bankCardId;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 目标类型
     */
    private Integer targetType;

    /**
     * 目标类型名称
     */
    private String targetTypeName;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 业务类别
     */
    private Integer bizCategory;

    /**
     * 业务类别名称
     */
    private String bizCategoryName;

    /**
     * 转账金额
     */
    private BigDecimal amount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 备注
     */
    private String remark;
}