package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 收款记录列表视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRecordsVo {
    
    /**
     * 收款记录列表
     */
    private List<ReceiptRecordVo> items;
} 