package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("discount_strategy")
public class DiscountStrategy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("strategy_name")
    private String strategyName;

    @TableField("strategy_type")
    private String strategyType;

    @TableField("discount_type")
    private String discountType;

    @TableField("discount_rate")
    private BigDecimal discountRate;

    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @TableField("min_amount")
    private BigDecimal minAmount;

    @TableField("max_discount")
    private BigDecimal maxDiscount;

    @TableField("target_cities")
    private String targetCities;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("usage_limit")
    private Integer usageLimit;

    @TableField("used_count")
    private Integer usedCount;

    @TableField("status")
    private String status;

    @TableField("created_by")
    private Long createdBy;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


}
