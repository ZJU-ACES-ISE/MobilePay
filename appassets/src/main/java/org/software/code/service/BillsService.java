package org.software.code.service;

import org.software.code.vo.BillsListVo;
import org.software.code.vo.BillsSummaryVo;

import java.util.List;

public interface BillsService {
    /**
     * 账单总览展示
     *
     * @param userId 用户id
     * @param year 年
     * @param month 月
     */
    BillsSummaryVo getBillsSummary(Long userId, String year, String month);


    /**
     * 账单明细数据列表展示
     *
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 账单类型
     * @param scene 场景
     * @param keyString 模糊搜索账单明细展示请求数据
     */
    List<BillsListVo> getBillsList(Long userId,String startTime,String endTime,Integer type,String scene,String keyString, Integer num);
}
