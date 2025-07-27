package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * UserSearchDto 是用户搜索条件数据传输对象，用于封装用户搜索的查询条件
 *
 */
@Data
public class UserSearchDto {

    /**
     * 页码
     */
    @Min(value = 1, message = "页码必须大于0")
    @JsonProperty("page_num")
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    @Min(value = 1, message = "页大小必须大于0")
    @Max(value = 100, message = "页大小不能超过100")
    @JsonProperty("page_size")
    private Integer pageSize = 20;

    /**
     * 关键字搜索（手机号、用户名、真实姓名）
     */
    @JsonProperty("keyword")
    private String keyword;

    /**
     * 用户状态：PENDING, APPROVED, REJECTED, DISABLED
     */
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|DISABLED)$", message = "用户状态值不正确")
    @JsonProperty("status")
    private String status;

    /**
     * 注册开始时间
     */
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    /**
     * 注册结束时间
     */
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    /**
     * 排序字段
     */
    @JsonProperty("order_by")
    private String orderBy = "created_time";

    /**
     * 排序方向：ASC, DESC
     */
    @Pattern(regexp = "^(ASC|DESC)$", message = "排序方向只能是ASC或DESC")
    @JsonProperty("order_direction")
    private String orderDirection = "DESC";
}