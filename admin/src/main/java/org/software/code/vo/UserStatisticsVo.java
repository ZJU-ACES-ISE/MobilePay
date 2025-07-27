package org.software.code.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 用户统计数据VO
 */
@Data
@Builder
public class UserStatisticsVo {

    private Long totalUsers;

    private Long pendingUsers;

    private Long approvedUsers;

    private Long rejectedUsers;

    private Long disabledUsers;

    private Long newUsersToday;

    private Long newUsersThisWeek;

    private Long newUsersThisMonth;
}