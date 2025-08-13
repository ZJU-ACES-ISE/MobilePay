/*
 Navicat Premium Dump SQL

 Source Server         : a6000-mobilepay
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : graywolf.top:6200
 Source Schema         : mobilepay

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 01/08/2025 16:46:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ADMIN' COMMENT '角色: SUPER_ADMIN, ADMIN',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE, LOCKED',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后登录IP',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_admin_username` (`username`),
  KEY `idx_admin_status` (`status`),
  KEY `idx_admin_role` (`role`),
  KEY `idx_admin_created_time` (`created_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for bank_card
-- ----------------------------
DROP TABLE IF EXISTS `bank_card`;
CREATE TABLE `bank_card` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '银行卡记录ID，主键',
  `user_id` bigint NOT NULL COMMENT '所属用户ID',
  `card_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '持卡人姓名（实名信息）',
  `card_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行预留手机号',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行名称（如“招商银行”）',
  `card_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行卡号（建议脱敏展示，仅用于系统识别）',
  `type` tinyint DEFAULT '1' COMMENT '银行卡类型：1=储蓄卡，2=信用卡',
  `status` tinyint NOT NULL DEFAULT '2' COMMENT '是否为默认卡：1=默认，2=非默认',
  `bind_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `last_four_digits` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (substr(`card_number`,-(4))) STORED COMMENT '卡号尾号，自动生成，用于展示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `card_number` (`card_number`)
) ENGINE=InnoDB AUTO_INCREMENT=1950735329678807042 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户银行卡表（卡包）';

-- ----------------------------
-- Table structure for discount_strategy
-- ----------------------------
DROP TABLE IF EXISTS `discount_strategy`;
CREATE TABLE `discount_strategy` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `strategy_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '策略名称',
  `strategy_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '策略类型: TRAVEL, PAYMENT, NEW_USER, HOLIDAY',
  `discount_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '折扣类型: PERCENTAGE, FIXED_AMOUNT, LADDER',
  `discount_rate` decimal(5,2) DEFAULT NULL COMMENT '折扣率(百分比)',
  `discount_amount` decimal(10,2) DEFAULT NULL COMMENT '折扣金额',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低消费金额',
  `max_discount` decimal(10,2) DEFAULT NULL COMMENT '最大折扣金额',
  `target_cities` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标城市(JSON数组)',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `usage_limit` int DEFAULT NULL COMMENT '使用限制次数',
  `used_count` int DEFAULT '0' COMMENT '已使用次数',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE, EXPIRED',
  `created_by` bigint DEFAULT NULL COMMENT '创建者管理员ID',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_type` (`strategy_type`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `discount_strategy_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `admin` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for receipt_code
-- ----------------------------
DROP TABLE IF EXISTS `receipt_code`;
CREATE TABLE `receipt_code` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `receipt_code_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收款码唯一编号',
  `user_id` bigint NOT NULL COMMENT '收款用户ID',
  `code_url` varchar(255) NOT NULL COMMENT '二维码URL',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '预设收款金额（可为空）',
  `expire_at` datetime NOT NULL COMMENT '二维码过期时间',
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间（二维码创建时间）',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `receipt_code_id` (`receipt_code_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收款码信息表';

-- ----------------------------
-- Table structure for receipt_transaction
-- ----------------------------
DROP TABLE IF EXISTS `receipt_transaction`;
CREATE TABLE `receipt_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transaction_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易编号，如 T202507150001',
  `payer_id` bigint NOT NULL COMMENT '付款用户ID',
  `receiver_id` bigint NOT NULL COMMENT '收款用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收款人姓名',
  `amount` decimal(10,2) NOT NULL COMMENT '收款金额',
  `receipt_code_id` varchar(20) DEFAULT NULL COMMENT '对应收款码ID（可为空）',
  `timestamp` datetime NOT NULL COMMENT '收款完成时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收款交易记录表';

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `site_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '站点编码',
  `site_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '站点名称',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市',
  `city_code` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市行政区划代码',
  `line_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线路名称',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT 'SUBWAY' COMMENT '类型：SUBWAY, BUS',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE, MAINTENANCE',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `site_code` (`site_code`),
  KEY `idx_site_code` (`site_code`),
  KEY `idx_site_status` (`status`),
  KEY `idx_site_city` (`city`),
  KEY `idx_site_line_name` (`line_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3058 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for transfer_record
-- ----------------------------
DROP TABLE IF EXISTS `transfer_record`;
CREATE TABLE `transfer_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '交易流水主键ID',
  `transfer_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '交易编号（平台唯一标识）',
  `user_id` bigint NOT NULL COMMENT '发起人用户ID',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起人用户名（冗余）',
  `type` tinyint NOT NULL COMMENT '交易类型：1=转入，2=转出',
  `bank_card_id` bigint DEFAULT NULL COMMENT '银行卡ID',
  `target_id` bigint DEFAULT NULL COMMENT '交易对象ID（用户ID或商户ID）',
  `target_type` tinyint DEFAULT NULL COMMENT '交易对象类型：1=用户，2=商户，3=银行卡',
  `target_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易对象名称（如银行卡、用户名、商户名）',
  `biz_category` tinyint DEFAULT '4' COMMENT '交易分类，1=餐饮，2=出行，3=购物，4=其他',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '费用',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '折扣金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际扣费金额',
  `complete_time` datetime DEFAULT NULL COMMENT '成功完成时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息（如风控标识）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `transfer_number` (`transfer_number`)
) ENGINE=InnoDB AUTO_INCREMENT=1950737585161588738 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- ----------------------------
-- Table structure for site_fare
-- ----------------------------
DROP TABLE IF EXISTS `site_fare`;
CREATE TABLE `site_fare`  (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `from_site_id` bigint NOT NULL COMMENT '起始站点ID',
    `to_site_id` bigint NOT NULL COMMENT '终点站点ID',
    `city_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市编码',
    `transit_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交通类型（SUBWAY地铁，BUS公交）',
    `base_fare` decimal(10, 2) NOT NULL COMMENT '基础票价',
    `distance` double NULL DEFAULT NULL COMMENT '距离（公里）',
    `station_count` int NULL DEFAULT NULL COMMENT '站点数量',
    `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '状态（ACTIVE正常，INACTIVE停用）',
    `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_from_to_site`(`from_site_id` ASC, `to_site_id` ASC, `transit_type` ASC) USING BTREE,
    INDEX `idx_from_site_id`(`from_site_id` ASC) USING BTREE,
    INDEX `idx_to_site_id`(`to_site_id` ASC) USING BTREE,
    INDEX `idx_city_code`(`city_code` ASC) USING BTREE,
    CONSTRAINT `site_fare_ibfk_1` FOREIGN KEY (`from_site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `site_fare_ibfk_2` FOREIGN KEY (`to_site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for transit_record
-- ----------------------------
DROP TABLE IF EXISTS `transit_record`;
CREATE TABLE `transit_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '出行方式(如地铁/公交)',
  `entry_site_id` bigint NOT NULL COMMENT '入站站点ID',
  `exit_site_id` bigint DEFAULT NULL COMMENT '出站站点ID',
  `entry_device_id` bigint DEFAULT NULL COMMENT '入站设备ID',
  `exit_device_id` bigint DEFAULT NULL COMMENT '出站设备ID',
  `entry_time` timestamp NOT NULL COMMENT '入站时间',
  `exit_time` timestamp NULL DEFAULT NULL COMMENT '出站时间',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '费用',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '折扣金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际扣费金额',
  `status` int NOT NULL COMMENT '出站状态（0正常，1支付异常，2出行异常）',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常原因（status为1或2时记录）',
  `transaction_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易记录编号（正常出站时记录）',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_entry_site_id` (`entry_site_id`),
  KEY `idx_exit_site_id` (`exit_site_id`),
  KEY `idx_entry_time` (`entry_time`),
  KEY `idx_status` (`status`),
  KEY `entry_device_id` (`entry_device_id`),
  KEY `exit_device_id` (`exit_device_id`),
  CONSTRAINT `transit_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transit_record_ibfk_2` FOREIGN KEY (`entry_site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transit_record_ibfk_3` FOREIGN KEY (`exit_site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transit_record_ibfk_4` FOREIGN KEY (`entry_device_id`) REFERENCES `turnstile_device` (`id`) ON DELETE SET NULL,
  CONSTRAINT `transit_record_ibfk_5` FOREIGN KEY (`exit_device_id`) REFERENCES `turnstile_device` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- ----------------------------
-- Table structure for transit_pass
-- ----------------------------
DROP TABLE IF EXISTS `transit_pass`;
CREATE TABLE `transit_pass`  (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `user_id` bigint NOT NULL COMMENT '用户ID',
 `city_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市ID',
 `city_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市名称',
 `code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通行码链接',
 `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '状态（ACTIVE正常，INACTIVE停用）',
 `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE INDEX `uk_user_city`(`user_id` ASC, `city_id` ASC) USING BTREE,
 INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
 INDEX `idx_city_id`(`city_id` ASC) USING BTREE,
 CONSTRAINT `transit_pass_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
-- ----------------------------
-- Table structure for transit_record
-- ----------------------------
DROP TABLE IF EXISTS `transit_record`;
CREATE TABLE `transit_record`  (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `user_id` bigint NOT NULL COMMENT '用户ID',
   `mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出行方式(如地铁/公交)',
   `entry_site_id` bigint NOT NULL COMMENT '入站站点ID',
   `exit_site_id` bigint NULL DEFAULT NULL COMMENT '出站站点ID',
   `entry_device_id` bigint NULL DEFAULT NULL COMMENT '入站设备ID',
   `exit_device_id` bigint NULL DEFAULT NULL COMMENT '出站设备ID',
   `entry_time` timestamp NOT NULL COMMENT '入站时间',
   `exit_time` timestamp NULL DEFAULT NULL COMMENT '出站时间',
   `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '费用',
   `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '折扣金额',
   `actual_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际扣费金额',
   `status` int NOT NULL COMMENT '出站状态（0正常，1支付异常，2出行异常）',
   `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异常原因（status为1或2时记录）',
   `transaction_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易记录编号（正常出站时记录）',
   `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`) USING BTREE,
   INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
   INDEX `idx_entry_site_id`(`entry_site_id` ASC) USING BTREE,
   INDEX `idx_exit_site_id`(`exit_site_id` ASC) USING BTREE,
   INDEX `idx_entry_time`(`entry_time` ASC) USING BTREE,
   INDEX `idx_status`(`status` ASC) USING BTREE,
   INDEX `entry_device_id`(`entry_device_id` ASC) USING BTREE,
   INDEX `exit_device_id`(`exit_device_id` ASC) USING BTREE,
   CONSTRAINT `transit_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
   CONSTRAINT `transit_record_ibfk_2` FOREIGN KEY (`entry_site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
   CONSTRAINT `transit_record_ibfk_3` FOREIGN KEY (`exit_site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
   CONSTRAINT `transit_record_ibfk_4` FOREIGN KEY (`entry_device_id`) REFERENCES `turnstile_device` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
   CONSTRAINT `transit_record_ibfk_5` FOREIGN KEY (`exit_device_id`) REFERENCES `turnstile_device` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for transit_repay
-- ----------------------------
DROP TABLE IF EXISTS `transit_repay`;
CREATE TABLE `transit_repay` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transit_id` varchar(30) NOT NULL COMMENT '出行记录ID',
  `amount` decimal(10,2) NOT NULL COMMENT '补缴金额',
  `pay_time` datetime NOT NULL COMMENT '补缴支付时间',
  `transaction_id` varchar(30) NOT NULL COMMENT '补缴交易ID',
  `cleared_at` datetime NOT NULL COMMENT '异常清除时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出行异常补缴记录表';

-- ----------------------------
-- Table structure for turnstile_device
-- ----------------------------
DROP TABLE IF EXISTS `turnstile_device`;
CREATE TABLE `turnstile_device` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备编码',
  `site_id` bigint NOT NULL COMMENT '站点ID',
  `device_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型: ENTRY, EXIT, BOTH',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE, OFFLINE, MAINTENANCE',
  `last_heartbeat` timestamp NULL DEFAULT NULL COMMENT '最后心跳时间',
  `firmware_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '固件版本',
  `type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT 'SUBWAY' COMMENT '类型：SUBWAY, BUS',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_code` (`device_code`),
  KEY `idx_site_id` (`site_id`),
  KEY `idx_device_code` (`device_code`),
  KEY `idx_status` (`status`),
  CONSTRAINT `turnstile_device_ibfk_1` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3058 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `login_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'PENDING' COMMENT '状态: PENDING, APPROVED, REJECTED, DISABLED',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  KEY `idx_user_phone` (`phone`),
  KEY `idx_user_status` (`status`),
  KEY `idx_user_created_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for user_audit_record
-- ----------------------------
DROP TABLE IF EXISTS `user_audit_record`;
CREATE TABLE `user_audit_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `audit_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核类型: REGISTER, PROFILE_UPDATE',
  `audit_result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'PENDING' COMMENT '审核结果: APPROVED, REJECTED',
  `audit_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '审核原因',
  `old_data` json DEFAULT NULL COMMENT '修改前数据',
  `new_data` json DEFAULT NULL COMMENT '修改后数据',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_admin_id` (`admin_id`),
  KEY `idx_created_time` (`created_time`),
  CONSTRAINT `user_audit_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_audit_record_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for user_balance
-- ----------------------------
DROP TABLE IF EXISTS `user_balance`;
CREATE TABLE `user_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID（唯一）',
  `balance` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '用户当前余额',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户余额表';

-- ----------------------------
-- Table structure for user_verification
-- ----------------------------
DROP TABLE IF EXISTS `user_verification`;
CREATE TABLE `user_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_back` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'pending',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `submit_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `audit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
