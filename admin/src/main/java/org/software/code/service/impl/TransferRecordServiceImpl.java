package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.entity.TransferRecord;
import org.software.code.mapper.TransferRecordMapper;
import org.software.code.service.TransferRecordService;
import org.software.code.vo.TransferRecordVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 转账记录服务实现类
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class TransferRecordServiceImpl extends ServiceImpl<TransferRecordMapper, TransferRecord> implements TransferRecordService {
    private static final Logger logger = LoggerFactory.getLogger(TransferRecordServiceImpl.class);

    @Resource
    private TransferRecordMapper transferRecordMapper;

    @Override
    public Page<TransferRecordVo> getUserTransferRecords(Long userId, Integer pageNum, Integer pageSize) {
        logger.info("获取用户转账记录，用户ID：{}，页码：{}，页大小：{}", userId, pageNum, pageSize);

        Page<TransferRecord> transferRecordPage = new Page<>(pageNum, pageSize);
        Page<TransferRecord> resultPage = transferRecordMapper.selectPage(
            transferRecordPage,
            Wrappers.<TransferRecord>lambdaQuery()
                .eq(TransferRecord::getUserId, userId)
                .orderByDesc(TransferRecord::getCompleteTime)
        );

        Page<TransferRecordVo> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setPages(resultPage.getPages());
        
        List<TransferRecordVo> transferRecordVos = resultPage.getRecords().stream()
                .map(this::convertToTransferRecordVo)
                .collect(Collectors.toList());
        
        voPage.setRecords(transferRecordVos);
        return voPage;
    }

    @Override
    public Page<TransferRecordVo> getAllTransferRecords(Integer pageNum, Integer pageSize) {
        logger.info("获取所有转账记录，页码：{}，页大小：{}", pageNum, pageSize);

        Page<TransferRecord> transferRecordPage = new Page<>(pageNum, pageSize);
        Page<TransferRecord> resultPage = transferRecordMapper.selectPage(
            transferRecordPage,
            Wrappers.<TransferRecord>lambdaQuery()
                .orderByDesc(TransferRecord::getCompleteTime)
        );

        Page<TransferRecordVo> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setPages(resultPage.getPages());
        
        List<TransferRecordVo> transferRecordVos = resultPage.getRecords().stream()
                .map(this::convertToTransferRecordVo)
                .collect(Collectors.toList());
        
        voPage.setRecords(transferRecordVos);
        return voPage;
    }

    private TransferRecordVo convertToTransferRecordVo(TransferRecord transferRecord) {
        String typeName = getTypeName(transferRecord.getType());
        String targetTypeName = getTargetTypeName(transferRecord.getTargetType());
        String bizCategoryName = getBizCategoryName(transferRecord.getBizCategory());
        
        return TransferRecordVo.builder()
                .transferNumber(transferRecord.getTransferNumber())
                .userId(transferRecord.getUserId())
                .userName(transferRecord.getUserName())
                .type(transferRecord.getType())
                .typeName(typeName)
                .bankCardId(transferRecord.getBankCardId())
                .targetId(transferRecord.getTargetId())
                .targetType(transferRecord.getTargetType())
                .targetTypeName(targetTypeName)
                .targetName(transferRecord.getTargetName())
                .bizCategory(transferRecord.getBizCategory())
                .bizCategoryName(bizCategoryName)
                .amount(transferRecord.getAmount())
                .discountAmount(transferRecord.getDiscountAmount())
                .actualAmount(transferRecord.getActualAmount())
                .completeTime(transferRecord.getCompleteTime())
                .remark(transferRecord.getRemark())
                .build();
    }
    
    private String getTypeName(Integer type) {
        if (type == null) {
            return null;
        };
        switch (type) {
            case 1: return "转入";
            case 2: return "转出";
            default: return "未知类型";
        }
    }
    
    private String getTargetTypeName(Integer targetType) {
        if (targetType == null) {
            return null;
        }
        switch (targetType) {
            case 1: return "用户";
            case 2: return "商户";
            case 3: return "银行卡";
            default: return "未知类型";
        }
    }
    
    private String getBizCategoryName(Integer bizCategory) {
        if (bizCategory == null) {
            return null;
        }
        switch (bizCategory) {
            case 1: return "餐饮";
            case 2: return "出行";
            case 3: return "购物";
            default: return "其他";
        }
    }
}