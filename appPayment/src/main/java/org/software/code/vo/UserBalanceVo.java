package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户余额视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceVo {
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 账户余额
     */
    private String balance;
} 