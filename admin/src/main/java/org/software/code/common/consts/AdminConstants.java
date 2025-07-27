package org.software.code.common.consts;

/**
 * AdminConstants 类是管理员系统常量定义类，用于定义系统中使用的各类常量
 *
 */
public class AdminConstants {

    /**
     * 管理员角色常量
     */
    public static final class AdminRole {
        public static final String SUPER_ADMIN = "SUPER_ADMIN";
        public static final String ADMIN = "ADMIN";
    }

    /**
     * 管理员状态常量
     */
    public static final class AdminStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String LOCKED = "LOCKED";
    }

    /**
     * 用户状态常量
     */
    public static final class UserStatus {
        public static final String PENDING = "PENDING";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String DISABLED = "DISABLED";
    }


    /**
     * 审核类型常量
     */
    public static final class AuditType {
        public static final String REGISTER = "REGISTER";
        public static final String PROFILE_UPDATE = "PROFILE_UPDATE";
    }

    /**
     * 审核结果常量
     */
    public static final class AuditResult {
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }

    /**
     * 设备类型常量
     */
    public static final class DeviceType {
        public static final String ENTRY = "ENTRY";
        public static final String EXIT = "EXIT";
        public static final String BOTH = "BOTH";
    }

    /**
     * 设备状态常量
     */
    public static final class DeviceStatus {
        public static final String ONLINE = "ONLINE";
        public static final String OFFLINE = "OFFLINE";
        public static final String MAINTENANCE = "MAINTENANCE";
        public static final String FAULT = "FAULT";
    }

    /**
     * 站点状态常量
     */
    public static final class SiteStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String MAINTENANCE = "MAINTENANCE";
    }

    /**
     * 站点类型常量
     */
    public static final class SiteType {
        public static final String MAIN = "MAIN";
        public static final String BRANCH = "BRANCH";
    }

    /**
     * 折扣策略状态常量
     */
    public static final class DiscountStrategyStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String EXPIRED = "EXPIRED";
    }

    /**
     * 折扣策略类型常量
     */
    public static final class DiscountStrategyType {
        public static final String TRAVEL = "TRAVEL";
        public static final String PAYMENT = "PAYMENT";
        public static final String NEW_USER = "NEW_USER";
        public static final String HOLIDAY = "HOLIDAY";
    }

    /**
     * 折扣类型常量
     */
    public static final class DiscountType {
        public static final String PERCENTAGE = "PERCENTAGE";
        public static final String FIXED_AMOUNT = "FIXED_AMOUNT";
        public static final String LADDER = "LADDER";
    }

    /**
     * JWT相关常量
     */
    public static final class JWT {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
        public static final String CLAIM_KEY_ADMIN_ID = "adminId";
        public static final String CLAIM_KEY_ROLE = "role";
        public static final String CLAIM_KEY_USERNAME = "username";
        public static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 24小时
        public static final long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L; // 7天
    }

    /**
     * Redis缓存key前缀常量
     */
    public static final class RedisKey {
        public static final String ADMIN_TOKEN_PREFIX = "admin:token:";
        public static final String ADMIN_LOGIN_FAIL_PREFIX = "admin:login:fail:";
        public static final String USER_CACHE_PREFIX = "user:";
        public static final String SITE_CACHE_PREFIX = "site:";
        public static final String DEVICE_CACHE_PREFIX = "device:";
    }

    /**
     * API路径常量
     */
    public static final class ApiPath {
        public static final String ADMIN_AUTH = "/admin/auth";
        public static final String ADMIN_USERS = "/admin/users";
        public static final String ADMIN_SITES = "/admin/sites";
        public static final String ADMIN_DEVICES = "/admin/devices";
        public static final String ADMIN_DISCOUNTS = "/admin/discounts";
        public static final String ADMIN_STATISTICS = "/admin/statistics";
    }

    /**
     * 默认值常量
     */
    public static final class DefaultValue {
        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
        public static final int LOGIN_FAIL_MAX_TIMES = 5;
        public static final long LOGIN_FAIL_LOCK_TIME = 30 * 60 * 1000L; // 30分钟
    }

    private AdminConstants() {
        // 私有构造器，防止实例化
    }
}