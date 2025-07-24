package org.software.code.vo;

import lombok.Data;

/**
 * CardsShowVo 是银行卡展示视图对象
 * 用于前端展示卡类型、银行名称、尾号、是否默认等信息
 *
 * @author
 */
@Data
public class CardsShowVo {

    /**
     * 银行卡唯一标识
     */
    private Long id;

    /**
     * 银行名称（如“招商银行”）
     */
    private String bankName;

    /**
     * 银行卡类型：1=储蓄卡，2=信用卡
     */
    private Integer type;

    /**
     * 银行卡类型名
     */
    private String typeName;

    /**
     * 是否为默认卡：1=默认，2=非默认
     */
    private Integer status;

    /**
     * 卡号尾号（自动生成字段）
     */
    private String lastFourDigits;
}
