package org.software.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.entity.TransitRecord;
import org.software.code.vo.TransitRecordVo;

public interface TransitRecordService extends IService<TransitRecord> {

    Page<TransitRecordVo> getUserTransitRecords(Long userId, Integer pageNum, Integer pageSize);
    
    Page<TransitRecordVo> getAllTransitRecords(Integer pageNum, Integer pageSize);
}
