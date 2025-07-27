package org.software.code.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.entity.TransferRecord;
import org.software.code.mapper.BillsMapper;
import org.software.code.service.BillsService;
import org.software.code.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class BillsServiceImpl implements BillsService {
    @Autowired
    private BillsMapper billsMapper;

    private String getTypeName(Integer type) {
        if (type == null) return "未知";
        return switch (type) {
            case 1 -> "收入";
            case 2 -> "转出";
            default -> "未知";
        };
    }

    private String getCategoryName(Integer category) {
        return switch (category) {
            case 1 -> "餐饮";
            case 2 -> "出行";
            case 3 -> "购物";
            case 4 -> "其他";
            default -> "未知";
        };
    }

    private String getTargetTypeName(Integer type) {
        return switch (type) {
            case 1 -> "用户";
            case 2 -> "商户";
            case 3 -> "银行卡";
            default -> "未知";
        };
    }

    @Override
    public BillsSummaryVo getBillsSummary(Long userId, String year, String month) {
        String monthStart = year + "-" + String.format("%02d", Integer.parseInt(month)) + "-01";
        LocalDate start = LocalDate.parse(monthStart);
        LocalDate end = start.plusMonths(1);

        //根据年月查询所有账单
        List<TransferRecord> records = billsMapper.selectList(new LambdaQueryWrapper<TransferRecord>()
                .eq(TransferRecord::getUserId, userId)
                .ge(TransferRecord::getCompleteTime, start)
                .lt(TransferRecord::getCompleteTime, end)
        );

        //统计收入支出
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<Integer, BigDecimal> expenseMap = new HashMap<>();

        for (TransferRecord record : records) {
            if (record.getType() == 1) { // 转入
                totalIncome = totalIncome.add(record.getAmount());
            } else if (record.getType() == 2) { // 转出
                totalExpense = totalExpense.add(record.getAmount());
                int category = record.getBizCategory() != null ? record.getBizCategory() : 4;
                expenseMap.put(category,
                        expenseMap.getOrDefault(category, BigDecimal.ZERO).add(record.getAmount()));
            }
        }

        BigDecimal balance = totalIncome.subtract(totalExpense);

        //将expenseMap提取为List<ExpenseCategoryVo> expenseVos
        List<ExpenseCategoryVo> expenseVos = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entry : expenseMap.entrySet()) {
            ExpenseCategoryVo vo = new ExpenseCategoryVo();
            vo.setBizCategory(entry.getKey());
            vo.setBizCategoryName(getCategoryName(entry.getKey()));
            vo.setAmount(entry.getValue().toPlainString());

            BigDecimal percent = entry.getValue().multiply(BigDecimal.valueOf(100))
                    .divide(totalExpense.equals(BigDecimal.ZERO) ? BigDecimal.ONE : totalExpense, 2, RoundingMode.HALF_UP);
            vo.setPercentage(percent.toPlainString());

            expenseVos.add(vo);
        }

        BillsSummaryVo summary = new BillsSummaryVo();
        summary.setId(userId);
        summary.setTotalIncome(totalIncome.toPlainString());
        summary.setTotalExpense(totalExpense.toPlainString());
        summary.setBalance(balance.toPlainString());
        summary.setExpenseV0(expenseVos);

        return summary;
    }

    @Override
    public List<BillsListVo> getBillsList(Long userId,String startTime,String endTime,Integer type,String scene,String keyString, Integer num) {
        QueryWrapper<TransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        if(StringUtils.isNotBlank(scene))
            if(scene.equals("bills")) //bills时添加条件
            {
                if (StringUtils.isNotBlank(startTime))
                    wrapper.ge("complete_time", startTime);
                if (StringUtils.isNotBlank(endTime))
                    wrapper.le("complete_time", endTime);
                if (type != null) {
                    wrapper.eq("type", type);
                }
                if (StringUtils.isNotBlank(keyString)) {
                    wrapper.and(w -> w
                            .like("target_name", keyString)
                            .or().like("transfer_number", keyString)
                    );
                }
            }
            else{ //index时添加条件
                wrapper.last("LIMIT " + num);
            }

        wrapper.orderByDesc("complete_time");

        List<TransferRecord> records = billsMapper.selectList(wrapper);

        return records.stream().map(record -> {
            BillsListVo vo = new BillsListVo();
            vo.setId(record.getId());
            vo.setType(record.getType());
            vo.setTypeName(getTypeName(record.getType()));
            vo.setBizCategory(record.getBizCategory());
            vo.setBizCategoryName(getCategoryName(record.getBizCategory()));
            vo.setTargetName(record.getTargetName());
            vo.setTargetType(record.getTargetType());
            vo.setTargetTypeName(getTargetTypeName(record.getTargetType()));
            vo.setAmount(record.getAmount().toPlainString());
            vo.setCompleteTime(record.getCompleteTime().toString());
            vo.setRemark(record.getRemark());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public YearBillsSummaryVo getYearSummary(Long userId, String year) {
        // 起止时间：2025-01-01 00:00:00 ~ 2025-12-31 23:59:59
        String start = year + "-01-01 00:00:00";
        String end = year + "-12-31 23:59:59";

        QueryWrapper<TransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .between("complete_time", start, end);

        List<TransferRecord> records = billsMapper.selectList(wrapper);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        int totalCount = records.size();

        Map<Integer, BigDecimal> categoryMap = new HashMap<>();
        Map<Integer, BigDecimal> monthlyExpenseMap = new HashMap<>();

        for (TransferRecord record : records) {
            if (record.getType() == 1) {
                totalIncome = totalIncome.add(record.getAmount());
            } else if (record.getType() == 2) {
                totalExpense = totalExpense.add(record.getAmount());

                // 支出分类金额
                categoryMap.merge(record.getBizCategory(), record.getAmount(), BigDecimal::add);

                // 月份支出统计
                int month = record.getCompleteTime().getMonthValue();
                monthlyExpenseMap.merge(month, record.getAmount(), BigDecimal::add);
            }
        }

        //将categoryMap提取为List<ExpenseCategoryVo> expenseVos
        BigDecimal finalTotalExpense = totalExpense;
        List<ExpenseCategoryVo> expenseVos = categoryMap.entrySet().stream()
                .map(entry -> {
                    ExpenseCategoryVo vo = new ExpenseCategoryVo();
                    vo.setBizCategory(entry.getKey());
                    vo.setBizCategoryName(getCategoryName(entry.getKey()));
                    vo.setAmount(entry.getValue().toPlainString());

                    BigDecimal percent = entry.getValue()
                            .multiply(BigDecimal.valueOf(100))
                            .divide(finalTotalExpense.equals(BigDecimal.ZERO) ? BigDecimal.ONE : finalTotalExpense, 2, RoundingMode.HALF_UP);
                    vo.setPercentage(percent.toString());
                    return vo;
                }).collect(Collectors.toList());

        List<ExpenseChartVo> chartVos = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    ExpenseChartVo chartVo = new ExpenseChartVo();
                    chartVo.setMonth(month);
                    BigDecimal monthExpense = monthlyExpenseMap.getOrDefault(month, BigDecimal.ZERO);
                    chartVo.setTotalExpense(monthExpense.toPlainString());
                    return chartVo;
                }).collect(Collectors.toList());

        YearBillsSummaryVo vo = new YearBillsSummaryVo();
        vo.setId(userId);
        vo.setTotalIncome(totalIncome.toPlainString());
        vo.setTotalExpense(totalExpense.toPlainString());
        vo.setTotalCount(totalCount);
        vo.setExpenseVos(expenseVos);
        vo.setExpenseChartVos(chartVos);

        return vo;
    }

    @Override
    public BillsDetailVo getBillsDetailById(Long id) {
        TransferRecord record = billsMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        BillsDetailVo vo = new BillsDetailVo();
        vo.setId(record.getId());
        vo.setTransferNumber(record.getTransferNumber());
        vo.setType(record.getType());
        vo.setTypeName(getTypeName(record.getType()));
        vo.setBizCategory(record.getBizCategory());
        vo.setBizCategoryName(getCategoryName(record.getBizCategory()));
        vo.setTargetName(record.getTargetName());
        vo.setTargetType(record.getTargetType());
        vo.setTargetTypeName(getTargetTypeName(record.getTargetType()));
        vo.setAmount(record.getAmount().toPlainString());
        vo.setCompleteTime(record.getCompleteTime().toString());
        vo.setRemark(record.getRemark());

        return vo;
    }

    @Override
    public void updateRemarkById(Long id, String remark) {
        TransferRecord record = billsMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        record.setRemark(remark);
        int rows = billsMapper.updateById(record);

        if (rows != 1) {
            throw new BusinessException(ExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void updateBizCategoryById(Long id, Integer bizCatogry) {
        TransferRecord record = billsMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        record.setBizCategory(bizCatogry);
        int rows = billsMapper.updateById(record);

        if (rows != 1) {
            throw new BusinessException(ExceptionEnum.UPDATE_FAILED);
        }
    }

}
