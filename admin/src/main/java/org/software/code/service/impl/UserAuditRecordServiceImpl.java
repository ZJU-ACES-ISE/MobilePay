package org.software.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.software.code.entity.UserAuditRecord;
import org.software.code.mapper.UserAuditRecordMapper;
import org.software.code.service.UserAuditRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class UserAuditRecordServiceImpl extends ServiceImpl<UserAuditRecordMapper, UserAuditRecord> implements UserAuditRecordService {

}
