package org.software.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.entity.TransferRecord;
import org.software.code.vo.TransferRecordVo;

public interface TransferRecordService extends IService<TransferRecord> {

    /**
     * 获取指定用户的转账记录
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页的转账记录
     */
    Page<TransferRecordVo> getUserTransferRecords(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取所有转账记录
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页的转账记录
     */
    Page<TransferRecordVo> getAllTransferRecords(Integer pageNum, Integer pageSize);
}