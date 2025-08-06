package org.software.code.ai.service;

import org.software.code.ai.mapper.TransactionRecordMapper;
import org.software.code.ai.model.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 交易记录服务
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    /**
     * 获取用户本月的交易记录
     */
    public Map<String, Object> getMonthlyTransactions(Long userId) {
        // 获取本月的开始和结束时间
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        
        // 获取交易记录
        List<TransactionRecord> transactions = transactionRecordMapper.listByUserIdAndTimeRange(
                userId, startDate, endDate);
        
        // 获取分类统计
        List<TransactionRecordMapper.CategoryStatistic> categoryStats = 
                transactionRecordMapper.getCategoryStatistics(userId, startDate, endDate);
        
        // 计算总收入、总支出
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .map(TransactionRecord::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .map(TransactionRecord::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("transactions", transactions);
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("categoryStatistics", categoryStats.stream()
                .map(stat -> {
                    Map<String, Object> statMap = new HashMap<>();
                    statMap.put("category", stat.getCategory());
                    statMap.put("totalAmount", stat.getTotalAmount());
                    statMap.put("transactionCount", stat.getTransactionCount());
                    return statMap;
                })
                .collect(Collectors.toList()));
        
        return result;
    }

    /**
     * 获取用户最近30天的异常交易
     */
    public List<TransactionRecord> getAbnormalTransactions(Long userId) {
        // 获取最近30天的交易记录
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Date startDate = Date.from(thirtyDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        List<TransactionRecord> transactions = transactionRecordMapper.listByUserIdAndTimeRange(
                userId, startDate, new Date());
        
        // 这里可以添加更复杂的异常检测逻辑
        // 例如：检测大额交易、高频交易等
        
        return transactions.stream()
                .filter(t -> isAbnormalTransaction(t))
                .collect(Collectors.toList());
    }
    
    private boolean isAbnormalTransaction(TransactionRecord transaction) {
        // 示例：检测大额交易（大于10000）
        if (transaction.getAmount() != null && 
            transaction.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            return true;
        }
        
        // 可以添加更多的异常检测逻辑
        // 例如：非常规时间交易、高频交易等
        
        return false;
    }
}
