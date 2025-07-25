package org.software.code.service;

import org.software.code.vo.BillsSummaryVo;

public interface BillsService {
    BillsSummaryVo getBillsSummary(Long userId, String year, String month);
}
