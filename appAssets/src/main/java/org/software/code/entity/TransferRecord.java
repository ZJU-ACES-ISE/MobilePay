package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransferRecord 实体类
 * 表示用户的交易流水记录，包括转入/转出信息、交易对象等。
 */
@Data
@TableName("transfer_record")
@Schema(name = "TransferRecord", description = "交易流水实体类")
public class TransferRecord implements Serializable {

    @TableId
    @Schema(description = "交易流水主键ID")
    private Long id;

    @Schema(description = "交易编号（平台唯一标识）")
    private String transferNumber;

    @Schema(description = "发起人用户ID")
    private Long userId;

    @Schema(description = "发起人用户名（冗余）")
    private String userName;

    @Schema(description = "交易类型：1=转入，2=转出")
    private Integer type;

    @Schema(description = "银行卡ID")
    private Long bankCardId;

    @Schema(description = "交易对象ID（用户ID或商户ID）")
    private Long targetId;

    @Schema(description = "交易对象类型：1=用户，2=商户，3=银行卡")
    private Integer targetType;

    @Schema(description = "交易对象名称（如银行卡、用户名、商户名）")
    private String targetName;

    @Schema(description = "交易分类：1=餐饮，2=出行，3=购物，4=其他")
    private Integer bizCategory;

    @Schema(description = "交易金额")
    private BigDecimal amount;

    @Schema(description = "成功完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "备注信息（如风控标识）")
    private String remark;

    private static final long serialVersionUID = 1L;
}
