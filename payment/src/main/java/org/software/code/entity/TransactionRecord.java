package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易流水记录实体类
 * 对应数据库表：transaction_record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("transaction_record")
public class TransactionRecord {

    /**
     * 交易流水主键ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 交易编号（平台内部唯一标识，可用于对账）
     */
    private String transferNumber;

    /**
     * 外键，主体用户ID
     */
    private Long userId;

    /**
     * 主体用户名（冗余，便于审计与展示）
     */
    private String userName;

    /**
     * 交易类型：1=转入，2=转出
     */
    private Integer type;

    /**
     * 外键，银行卡ID
     */
    private Long bankCardId;

    /**
     * 外键，交易对象ID（用户或商户）
     */
    private Long targetId;

    /**
     * 交易对象类型：1=用户，2=商户，3=银行卡
     */
    private Integer targetType;

    /**
     * 交易对象名称（银行卡名、用户、商户等）
     */
    private String targetName;

    /**
     * 分类，1=餐饮，2=出行，3=购物，4=其他（默认）
     */
    private Integer bizCategory;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 成功时间
     */
    private LocalDateTime completeTime;

    /**
     * 备注信息
     */
    private String remark;
}
