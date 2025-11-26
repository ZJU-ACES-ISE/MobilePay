/*
 Navicat Premium Dump SQL

 Source Server         : mobilepay
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : graywolf.top:6200
 Source Schema         : appUser

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 26/11/2025 13:47:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ADMIN' COMMENT '角色: SUPER_ADMIN, ADMIN',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE, LOCKED',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_admin_username`(`username` ASC) USING BTREE,
  INDEX `idx_admin_status`(`status` ASC) USING BTREE,
  INDEX `idx_admin_role`(`role` ASC) USING BTREE,
  INDEX `idx_admin_created_time`(`created_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$gz.AKntR/JgIuBFmnFiXmeYQr3meyQySkxkro61lErERypoeCplmS', 'SUPER_ADMIN', 'ACTIVE', '2025-10-09 02:58:28', '222.244.139.213', '2025-08-01 15:34:46', '2025-10-09 10:58:28');
INSERT INTO `admin` VALUES (2, 'manager', '$2a$10$GfXuN0KHicw0nSPj1nuOb.ZOt1L7gktnKZfs2NT6wMaTqRCwhE196', 'ADMIN', 'ACTIVE', NULL, NULL, '2025-08-01 15:34:46', '2025-08-01 15:34:46');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `login_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, APPROVED, REJECTED, DISABLED',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_user_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_user_status`(`status` ASC) USING BTREE,
  INDEX `idx_user_created_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (125, '13812345678', '$2a$10$2khufh/xQso349pdDSSxuuFCZTY4OHcknDbYv6KTh9ANkXVJlf796', '$2a$10$dAOh2.RWwC73tBv/RKeuQ.hNrBO.I6Ukie/8bVcvTa7dgThppDszK', '新昵称', 'https://mobilepaymike.oss-cn-beijing.aliyuncs.com/avatar/972057c7-de53-446a-903c-bdfce4d1659e.png', 'APPROVED', '2025-07-31 01:02:51', '2025-08-14 16:10:33');
INSERT INTO `user` VALUES (126, '13812345679', '$2a$10$BfU0FMBexu265yhz72oy2egZtC6nxtpHuoTgL7vIYVabOsQ9WWXhy', '$2a$10$g7sBhvGrk2nN.vnipXn1X.tTaSPDYrfiIwrsjdlvN60p70dsJITOC', '用户5679', '', 'APPROVED', '2025-07-27 00:48:31', '2025-08-01 19:37:32');
INSERT INTO `user` VALUES (128, '15717798453', '$2a$10$J2bnXdhYqp6DSf27IU5msO.SHEyKzN2de6sYafsxfrEygEA17k6e.', '$2a$10$g3nL4EEppuSc1u0hZuGj0.7qi1WMBtn4TQhy9oiJBjbM8ApmSxMI.', '用户8453', '', 'DISABLED', '2025-08-16 08:45:55', '2025-09-05 11:36:29');
INSERT INTO `user` VALUES (129, '15717798454', '$2a$10$PNSERx4QrWq3KWhn7S3NYOzUTyBJEiaN4Ert/fcsMssTqPdtebWJq', '$2a$10$qPVBPw/o5G8HwnioiWpt3ODQ4nDsRm1O8J27QME6ueclMEmu7vHlO', '用户8454', '', 'PENDING', '2025-08-23 15:38:51', '2025-08-23 15:38:51');
INSERT INTO `user` VALUES (130, '13812345608', '$2a$10$d2s9PDAAfAnC3Peug0548u0EM7cPJ17gvuIlB6GFVCcK8pb6o0V1m', '$2a$10$7wbkyq94mh9HwOsH1mwT0.ZO5GNFBEoMZ2vJX2udOvRaULgHvgxOm', '用户5608', '', 'PENDING', '2025-09-04 11:36:38', '2025-09-04 11:36:38');

-- ----------------------------
-- Table structure for user_audit_record
-- ----------------------------
DROP TABLE IF EXISTS `user_audit_record`;
CREATE TABLE `user_audit_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `audit_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核类型: REGISTER, PROFILE_UPDATE',
  `audit_result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'PENDING' COMMENT '审核结果: APPROVED, REJECTED',
  `audit_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '审核原因',
  `old_data` json NULL COMMENT '修改前数据',
  `new_data` json NULL COMMENT '修改后数据',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_admin_id`(`admin_id` ASC) USING BTREE,
  INDEX `idx_created_time`(`created_time` ASC) USING BTREE,
  CONSTRAINT `user_audit_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_audit_record_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_audit_record
-- ----------------------------
INSERT INTO `user_audit_record` VALUES (1, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-08 16:15:53');
INSERT INTO `user_audit_record` VALUES (2, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-08 16:15:57');
INSERT INTO `user_audit_record` VALUES (3, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-08 16:26:28');
INSERT INTO `user_audit_record` VALUES (4, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-08 16:26:30');
INSERT INTO `user_audit_record` VALUES (5, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-08 16:36:21');
INSERT INTO `user_audit_record` VALUES (6, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-08 16:36:33');
INSERT INTO `user_audit_record` VALUES (7, 125, 1, 'REGISTER', 'APPROVED', '审核通过', NULL, NULL, '2025-08-08 16:39:45');
INSERT INTO `user_audit_record` VALUES (8, 125, 1, 'REGISTER', 'REJECTED', '不通过', NULL, NULL, '2025-08-08 16:40:48');
INSERT INTO `user_audit_record` VALUES (9, 125, 1, 'REGISTER', 'APPROVED', '审核通过', NULL, NULL, '2025-08-08 16:44:48');
INSERT INTO `user_audit_record` VALUES (10, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-09 11:28:59');
INSERT INTO `user_audit_record` VALUES (11, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-09 11:45:21');
INSERT INTO `user_audit_record` VALUES (12, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-09 11:45:25');
INSERT INTO `user_audit_record` VALUES (13, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-09 11:57:54');
INSERT INTO `user_audit_record` VALUES (14, 125, 1, 'REGISTER', 'APPROVED', '正常放行', NULL, NULL, '2025-08-15 17:18:03');
INSERT INTO `user_audit_record` VALUES (15, 125, 1, 'REGISTER', 'APPROVED', 'string', NULL, NULL, '2025-08-15 17:29:57');
INSERT INTO `user_audit_record` VALUES (16, 125, 1, 'REGISTER', 'REJECTED', 'string', NULL, NULL, '2025-08-15 17:30:38');
INSERT INTO `user_audit_record` VALUES (17, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-21 15:59:53');
INSERT INTO `user_audit_record` VALUES (18, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-21 22:34:59');
INSERT INTO `user_audit_record` VALUES (19, 126, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-22 09:43:32');
INSERT INTO `user_audit_record` VALUES (20, 128, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-08-22 09:47:57');
INSERT INTO `user_audit_record` VALUES (21, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-09-12 13:49:44');
INSERT INTO `user_audit_record` VALUES (22, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-09-12 13:55:25');
INSERT INTO `user_audit_record` VALUES (23, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-09-12 14:01:12');
INSERT INTO `user_audit_record` VALUES (24, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-09-14 19:06:40');
INSERT INTO `user_audit_record` VALUES (25, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-09-14 22:42:51');
INSERT INTO `user_audit_record` VALUES (26, 125, 1, 'DISABLE', 'DISABLED', '管理员操作', NULL, NULL, '2025-10-07 13:34:31');

-- ----------------------------
-- Table structure for user_verification
-- ----------------------------
DROP TABLE IF EXISTS `user_verification`;
CREATE TABLE `user_verification`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_back` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'pending',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `submit_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `audit_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_verification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_verification
-- ----------------------------
INSERT INTO `user_verification` VALUES (1, 125, '啊啊啊', '123456789012345678', 'https://mobilepaymike.oss-cn-beijing.aliyuncs.com/audit/6dac581e-706e-44db-90c9-11d4b6a1f989_front.jpg', 'https://mobilepaymike.oss-cn-beijing.aliyuncs.com/audit/adc74245-f9ce-4779-9a23-caf72c82aa91_back.jpg', 'pending', '2025-07-27 08:23:38.000000 +00:00', '2025-08-15 16:57:03', '2025-08-04 11:31:12');
INSERT INTO `user_verification` VALUES (3, 128, '111', '123456789012345678', 'https://mobilepaymike.oss-cn-beijing.aliyuncs.com/audit/89d3dc5a-5130-4e28-be39-15592ae5752a_front.jpg', 'https://mobilepaymike.oss-cn-beijing.aliyuncs.com/audit/167fb0e1-cb8c-47a2-8208-ad26f72e4a46_back.jpg', 'approved', '', '2025-09-01 13:18:32', '2025-09-05 11:49:46');

SET FOREIGN_KEY_CHECKS = 1;
