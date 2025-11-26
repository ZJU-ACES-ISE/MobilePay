/*
 Navicat Premium Dump SQL

 Source Server         : mobilepay
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : graywolf.top:6200
 Source Schema         : appAssets

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 26/11/2025 13:46:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bank_card
-- ----------------------------
DROP TABLE IF EXISTS `bank_card`;
CREATE TABLE `bank_card`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '银行卡记录ID，主键',
  `user_id` bigint NOT NULL COMMENT '所属用户ID',
  `card_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '持卡人姓名（实名信息）',
  `card_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行预留手机号',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行名称（如“招商银行”）',
  `card_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行卡号（建议脱敏展示，仅用于系统识别）',
  `type` tinyint NULL DEFAULT 1 COMMENT '银行卡类型：1=储蓄卡，2=信用卡',
  `status` tinyint NOT NULL DEFAULT 2 COMMENT '是否为默认卡：1=默认，2=非默认',
  `bind_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `last_four_digits` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (substr(`card_number`,-(4))) STORED COMMENT '卡号尾号，自动生成，用于展示' NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `card_number`(`card_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1963603263001710595 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户银行卡表（卡包）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bank_card
-- ----------------------------
INSERT INTO `bank_card` VALUES (1948340255568666626, 125, '张三', '13800001111', '招商银行', '6225888888888888', 1, 1, '2025-07-24 19:11:30', DEFAULT);
INSERT INTO `bank_card` VALUES (1949662577261744129, 125, '张三', '13812345678', '招商银行', '6222021234567890123', 1, 2, '2025-07-28 10:45:56', DEFAULT);
INSERT INTO `bank_card` VALUES (1949664331726524418, 128, 'aaaa', '15717798453', 'xx银行', '1234567890123456', 1, 2, '2025-07-28 10:52:54', DEFAULT);
INSERT INTO `bank_card` VALUES (1949729803021012994, 128, '测试', '15717798453', 'xx银行', '0987654321654321', 1, 1, '2025-07-28 15:13:04', DEFAULT);
INSERT INTO `bank_card` VALUES (1950735329678807041, 128, '得到的', '15717798453', 'xx银行', '1236547890654213', 1, 2, '2025-07-31 09:48:40', DEFAULT);
INSERT INTO `bank_card` VALUES (1956283432238551041, 128, 'Aaa', '15717798453', 'xx银行', '1234567890123456789', 1, 2, '2025-08-15 17:14:51', DEFAULT);
INSERT INTO `bank_card` VALUES (1956367579619495937, 128, '张三', '13812312345', '宁波银行', '6222021234567111111', 1, 2, '2025-08-15 22:49:13', DEFAULT);
INSERT INTO `bank_card` VALUES (1958391109643112449, 128, '123', '15717798453', 'HNUBANK', '1234567890123456780', 1, 1, '2025-08-21 12:50:00', DEFAULT);
INSERT INTO `bank_card` VALUES (1959158653618458625, 129, '123', '15717798454', 'natural bank', '1234511111111111111', 1, 2, '2025-08-23 15:39:57', DEFAULT);

-- ----------------------------
-- Table structure for transfer_record
-- ----------------------------
DROP TABLE IF EXISTS `transfer_record`;
CREATE TABLE `transfer_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '交易流水主键ID',
  `transfer_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易编号（平台唯一标识）',
  `user_id` bigint NOT NULL COMMENT '发起人用户ID',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发起人用户名（冗余）',
  `type` tinyint NOT NULL COMMENT '交易类型：1=转入，2=转出',
  `bank_card_id` bigint NULL DEFAULT NULL COMMENT '银行卡ID',
  `target_id` bigint NULL DEFAULT NULL COMMENT '交易对象ID（用户ID或商户ID）',
  `target_type` tinyint NULL DEFAULT NULL COMMENT '交易对象类型：1=用户，2=商户，3=银行卡',
  `target_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易对象名称（如银行卡、用户名、商户名）',
  `biz_category` tinyint NULL DEFAULT 4 COMMENT '交易分类，1=餐饮，2=出行，3=购物，4=其他',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '费用',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '折扣金额',
  `actual_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际扣费金额',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '成功完成时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息（如风控标识）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `transfer_number`(`transfer_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1970476269591158786 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transfer_record
-- ----------------------------
INSERT INTO `transfer_record` VALUES (1948615289084264450, '39865f25-e391-44cc-b8f4-b984a274a091', 125, '用户1111', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 500.00, NULL, NULL, '2025-07-17 11:20:58', '备注内容');
INSERT INTO `transfer_record` VALUES (1948978604696330241, '0d910cde-202f-4039-b976-8388e996644f', 125, '用户1111', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 500.11, NULL, NULL, '2025-07-28 10:07:01', '用户提现');
INSERT INTO `transfer_record` VALUES (1949347580035096578, '1618fb85-6e21-41ba-83e1-cfe181ca0189', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, NULL, NULL, '2025-07-28 10:15:41', '用户充值');
INSERT INTO `transfer_record` VALUES (1949348001692672001, 'f5e6fbc2-f78c-4dca-ae77-f0cde5422855', 125, '新昵称', 2, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 300.00, NULL, NULL, '2025-07-28 10:16:56', '用户充值');
INSERT INTO `transfer_record` VALUES (1949652783842922498, '73602f0b-5c59-4256-99d1-dda333028c6a', 125, '新昵称', 2, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 300.00, NULL, NULL, '2025-07-28 15:39:18', '用户提现');
INSERT INTO `transfer_record` VALUES (1949654962355273729, '5d03b086-661e-4726-bdf0-085fc0ca3249', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, NULL, NULL, '2025-07-28 15:39:35', '用户充值');
INSERT INTO `transfer_record` VALUES (1949655278900154369, 'e0a70df1-48b2-4cb6-91a6-1dd68148ca61', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, NULL, NULL, '2025-07-29 10:17:09', '用户充值');
INSERT INTO `transfer_record` VALUES (1949736405824393218, '29cb2417-cd55-42c3-b523-85476a195677', 125, '新昵称', 2, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 300.00, NULL, NULL, '2025-07-29 10:25:10', '用户充值');
INSERT INTO `transfer_record` VALUES (1949736475886047233, 'd0db5060-e1e6-445c-8abc-12b4db68948d', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, NULL, NULL, '2025-07-29 10:25:35', '用户充值');
INSERT INTO `transfer_record` VALUES (1950017719777579010, '4fe7ab39-5b00-4896-84b3-858dd6257257', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, NULL, NULL, '2025-07-29 10:26:58', '用户提现');
INSERT INTO `transfer_record` VALUES (1950019740559691777, 'df115e67-2cf0-4fc5-9ba6-73cc1135c94e', 125, '新昵称', 1, 1949662577261744129, 1949662577261744129, 3, '招商银行', 4, 222.00, NULL, NULL, '2025-07-29 10:38:32', '用户提现');
INSERT INTO `transfer_record` VALUES (1950019844037365762, 'e0de6c9c-1ddf-4721-a9ef-cc062887d54f', 125, '新昵称', 1, 1949662577261744129, 1949662577261744129, 3, '招商银行', 4, 1.00, NULL, NULL, '2025-07-29 10:42:59', '用户充值');
INSERT INTO `transfer_record` VALUES (1950020191413817345, 'a8dd43b9-dc64-4809-a748-056fdcdce14c', 125, '新昵称', 2, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 300.00, NULL, NULL, '2025-07-30 02:43:22', '吃了');
INSERT INTO `transfer_record` VALUES (1950023102734749697, 'cf2463ae-2cf0-4509-a383-3e0fb9d1b133', 125, '新昵称', 2, 1949662577261744129, 1949662577261744129, 3, '招商银行', 4, 6666.00, NULL, NULL, '2025-07-31 09:57:29', '用户充值');
INSERT INTO `transfer_record` VALUES (1950024221565341698, '10a2e4ed-a19f-4a26-af29-f8ecfd8c0503', 125, '新昵称', 1, 1949662577261744129, 1949662577261744129, 3, '招商银行', 4, 5942.00, NULL, NULL, '2025-07-31 09:59:29', '用户充值');
INSERT INTO `transfer_record` VALUES (1950737545793851393, '8e5f807c-fc22-4b8a-b32a-a58f923e993d', 125, '新昵称', 1, 1949662577261744129, 1949662577261744129, 3, '招商银行', 4, 444.00, NULL, NULL, '2025-08-01 11:20:03', '用户充值');
INSERT INTO `transfer_record` VALUES (1950737585161588737, '35584f14-0b71-42d4-9396-468717ac2440', 125, '新昵称', 2, 1949662577261744129, 1949662577261744129, 3, '招商银行', 4, 8580.50, NULL, NULL, '2025-08-02 11:20:08', '用户充值');
INSERT INTO `transfer_record` VALUES (1950737585161588738, 'T17543858052625307DC', 125, '新昵称', 66, NULL, 44, 3, '抗丽萍', 57, 1.50, 0.00, NULL, '2025-08-05 17:23:25', '支付确认');
INSERT INTO `transfer_record` VALUES (1950737585161588739, 'T1754387785159EF8AD6', 125, '新昵称', 66, NULL, 44, 3, '抗丽萍', 57, 1.50, 0.00, NULL, '2025-08-05 17:56:25', '支付确认');
INSERT INTO `transfer_record` VALUES (1950737585161588740, 'T175438781215836D3AD', 125, '新昵称', 66, NULL, 44, 3, '抗丽萍', 57, 1.50, 0.00, NULL, '2025-08-05 17:56:52', '支付确认');
INSERT INTO `transfer_record` VALUES (1950737585161588741, 'T1754388074333CFAD3D', 125, '新昵称', 66, NULL, 44, 3, '抗丽萍', 57, 1.50, 0.00, NULL, '2025-08-05 18:01:15', '支付确认');
INSERT INTO `transfer_record` VALUES (1950737585161588742, 'T1754388141473B1895B', 125, '新昵称', 66, NULL, 44, 3, '抗丽萍', 57, 1.50, 0.00, NULL, '2025-08-05 18:02:22', '支付确认');
INSERT INTO `transfer_record` VALUES (1950737585161588743, 'T17543881759010A635E', 125, '新昵称', 66, NULL, 125, 3, '新昵称', 57, 1.50, 0.00, NULL, '2025-08-05 18:02:56', '支付确认');
INSERT INTO `transfer_record` VALUES (1950737585161588744, 'T1754389015486F26A41', 125, '新昵称', 66, NULL, 127, 1, '666', 1, 900.00, 0.00, NULL, '2025-08-05 18:16:56', '支付确认');
INSERT INTO `transfer_record` VALUES (1956246323515592706, '60bed140-df89-43f1-bbfd-7ec8c49c92d1', 125, '新昵称', 2, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 8580.50, 0.00, NULL, '2025-08-15 14:47:23', '用户提现');
INSERT INTO `transfer_record` VALUES (1958375861758431234, '4e013ad4-21fe-4bd7-a378-7ce9cddacaf7', 128, '用户8453', 1, 1956367579619495937, 1956367579619495937, 3, '宁波银行', 4, 111.00, 0.00, NULL, '2025-08-21 11:49:25', '用户充值');
INSERT INTO `transfer_record` VALUES (1958387457859751938, '4c816bc5-6ea4-4182-a95d-0aed2652a089', 128, '用户8453', 2, 1956367579619495937, 1956367579619495937, 3, '宁波银行', 4, 93.00, 0.00, NULL, '2025-08-21 12:35:30', '用户提现');
INSERT INTO `transfer_record` VALUES (1958389102710591490, '57be8658-9306-4fed-8e0d-46098e637a0e', 128, '用户8453', 1, 1956367579619495937, 1956367579619495937, 3, '宁波银行', 4, 100.00, 0.00, NULL, '2025-08-21 12:42:02', '用户充值');
INSERT INTO `transfer_record` VALUES (1958394559701422082, '432f18c5-4484-43da-8957-67ce09ff0ba6', 128, '用户8453', 2, 1950735329678807041, 1950735329678807041, 3, 'xx银行', 4, 94.00, 0.00, NULL, '2025-08-21 13:03:43', '用户提现');
INSERT INTO `transfer_record` VALUES (1959158712917528577, '687c52f5-3b1e-43da-816a-720d903f3a27', 129, '用户8454', 1, 1959158653618458625, 1959158653618458625, 3, 'natural bank', 4, 1.00, 0.00, NULL, '2025-08-23 15:40:11', '用户充值');
INSERT INTO `transfer_record` VALUES (1959158712917528578, 'T1756011564538EF30D2', 125, '新昵称', 66, NULL, 126, 1, '用户5679', 1, 9000.00, 0.00, NULL, '2025-08-24 04:59:25', '支付确认');
INSERT INTO `transfer_record` VALUES (1963442862226944001, '69e2e007-f60c-4c6a-a36e-ae5ed0a3860c', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-04 11:23:52', '支付支出');
INSERT INTO `transfer_record` VALUES (1963442983538798594, '327c2d91-3cd9-458a-9a26-33bcb984af63', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-04 11:24:21', '支付支出');
INSERT INTO `transfer_record` VALUES (1963443687514976257, '1f2f0f39-5234-47a9-9b0f-08e91539b36d', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-04 11:27:09', '支付支出');
INSERT INTO `transfer_record` VALUES (1963443790497722370, '4cdccb6e-3f35-4a43-a275-13a16a4fb51a', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-04 11:27:33', '支付支出');
INSERT INTO `transfer_record` VALUES (1963445793848307714, '7c4be9ef-9eab-4241-8795-4c1969e35818', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 3.50, 0.00, NULL, '2025-09-04 11:35:31', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963455037041553409, '835cff19-bda4-41ca-a81f-38133eeddc40', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-04 12:12:15', '支付支出');
INSERT INTO `transfer_record` VALUES (1963492356599640066, 'd89f3f79-19bd-4088-ba9e-9bd4b30dce97', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 3.50, 0.00, NULL, '2025-09-04 14:40:32', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963492372105981954, '4d42d44c-c1a5-4683-9d3c-721e2e359ea0', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 3.50, 0.00, NULL, '2025-09-04 14:40:36', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963597454842777602, 'a6fc6e89-ba4b-49bc-afca-b66e20c0c44c', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, 0.00, NULL, '2025-09-04 21:38:10', '用户充值');
INSERT INTO `transfer_record` VALUES (1963597770724200450, '8ef34b7b-e34a-426d-8976-bcef72e6a313', 125, '新昵称', 1, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 350.22, 0.00, NULL, '2025-09-04 21:39:25', '用户充值');
INSERT INTO `transfer_record` VALUES (1963598918097031169, '444b3b6e-509d-4a06-8a0b-85a2e952e3c9', 128, '用户8453', 1, 1956367579619495937, 1956367579619495937, 3, '招商银行', 4, 350.22, 0.00, NULL, '2025-09-04 21:43:59', '用户充值');
INSERT INTO `transfer_record` VALUES (1963599097067982850, 'e78ee1bd-3ad6-45df-87aa-52392931e841', 125, '新昵称', 2, 1948340255568666626, 1948340255568666626, 3, '招商银行', 4, 300.00, 0.00, NULL, '2025-09-04 21:44:41', '用户提现');
INSERT INTO `transfer_record` VALUES (1963599143394070529, '6f7af2df-766c-42b8-9f44-698b29753d73', 128, '用户8453', 2, 1956367579619495937, 1956367579619495937, 3, '招商银行', 3, 300.00, 0.00, NULL, '2025-09-05 13:44:52', '用户提现');
INSERT INTO `transfer_record` VALUES (1963791451360915458, '78376b6a-6126-4386-823d-fba0283399ba', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 3.50, 0.00, NULL, '2025-09-05 10:29:02', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963791538552107010, 'a33fcdc2-25c1-4fa5-8b77-2cfd3814038e', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 3.50, 0.00, NULL, '2025-09-05 10:29:23', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963791592885121026, '3be83c34-83c3-4120-be27-db4fac5818cf', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 3.50, 0.00, NULL, '2025-09-05 10:29:36', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963804041520599041, 'b57e70e2-d660-4c84-8146-a405048090c3', 128, '用户8453', 2, NULL, 128, 1, '用户8453', 4, 3.50, 0.00, NULL, '2025-09-05 11:19:04', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963806514394157057, '32bf7ffe-897c-4dc0-8d8f-02bfe0f8bcf1', 128, '用户8453', 2, NULL, 128, 1, '用户8453', 4, 3.50, 0.00, NULL, '2025-09-05 11:28:53', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1963808020694462465, 'a984cdcd-45ce-42d2-86dd-fe8b66259c12', 128, '用户8453', 2, NULL, 128, 1, '用户8453', 3, 9.00, 0.00, NULL, '2025-09-06 03:34:53', '支付支出');
INSERT INTO `transfer_record` VALUES (1963808568239878145, 'fad29414-7592-4dc1-9c1c-7a84edcdd785', 128, '用户8453', 2, NULL, 128, 1, '用户8453', 4, 2.00, 0.00, NULL, '2025-09-05 11:37:03', '交通出行费用');
INSERT INTO `transfer_record` VALUES (1966885669385240578, '347d3231-648c-4b87-9f9f-3a8ac7916f80', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-13 23:24:21', '支付支出');
INSERT INTO `transfer_record` VALUES (1966887209202319361, '86f61ec8-0449-4269-a22e-7ba9c1c0d366', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-13 23:30:28', '支付支出');
INSERT INTO `transfer_record` VALUES (1967147469070635010, '0761d4be-79dc-4ce5-93e2-71527854eb03', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 1.50, 0.00, NULL, '2025-09-14 16:44:39', '支付支出');
INSERT INTO `transfer_record` VALUES (1967147801448255489, 'f3a17779-ae4c-43a1-afd0-0b34d9a32c24', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 16:45:58', '支付支出');
INSERT INTO `transfer_record` VALUES (1967147870452944898, '73d1ed7c-0baf-4d14-948e-224fd15435a0', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:46:15', '支付支出');
INSERT INTO `transfer_record` VALUES (1967148074333868034, '0c74dc04-1882-482f-9932-32543e035ff7', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:47:03', '支付支出');
INSERT INTO `transfer_record` VALUES (1967148398268354562, 'b50cc62f-f05c-4606-9313-4d4440e469f9', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:48:21', '支付支出');
INSERT INTO `transfer_record` VALUES (1967148867288010754, '94364783-7308-4c32-8794-953ef78b1cc1', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:50:12', '支付支出');
INSERT INTO `transfer_record` VALUES (1967149046049247233, 'a85db197-db90-46e9-84a7-a8de022d5ad8', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:50:55', '支付支出');
INSERT INTO `transfer_record` VALUES (1967149473801146370, '61d7059a-f778-4861-bd14-df28563b56dd', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:52:37', '支付支出');
INSERT INTO `transfer_record` VALUES (1967149522882891777, '6e15e999-b92a-4d06-97dc-1138f232eb7c', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 100.00, 0.00, NULL, '2025-09-14 16:52:49', '支付支出');
INSERT INTO `transfer_record` VALUES (1967149602465615873, '14d15b27-384b-4160-8867-369cca67c3c4', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 16:53:08', '支付支出');
INSERT INTO `transfer_record` VALUES (1967149770082586625, '511ab93c-2a11-4338-8c3f-447971aca0d1', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 16:53:48', '支付支出');
INSERT INTO `transfer_record` VALUES (1967150218021670914, 'eda50aff-94e6-4b25-858c-512fa510d37a', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 16:55:35', '支付支出');
INSERT INTO `transfer_record` VALUES (1967150824811630593, 'ed31f3ee-f172-4097-acfb-2e03ebc690e9', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 16:57:59', '支付支出');
INSERT INTO `transfer_record` VALUES (1967151807637721089, 'ea8f7168-98bb-4fcd-8f56-8db8df90de01', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 17:01:54', '支付支出');
INSERT INTO `transfer_record` VALUES (1967153278752100354, 'cc3c6a61-5e8f-48d0-b9b6-d1edf99a61ee', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 17:07:44', '支付支出');
INSERT INTO `transfer_record` VALUES (1967153329108914177, '7854d9a8-5d5f-4133-b187-5192fc396719', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 9.00, 0.00, NULL, '2025-09-14 17:07:56', '支付支出');
INSERT INTO `transfer_record` VALUES (1970476269591158785, 'b5d40ec9-b724-425f-a76f-16f39f3cc7c6', 125, '新昵称', 2, NULL, 125, 1, '新昵称', 4, 90.00, 0.00, NULL, '2025-09-23 21:12:07', '支付支出');

-- ----------------------------
-- Table structure for user_balance
-- ----------------------------
DROP TABLE IF EXISTS `user_balance`;
CREATE TABLE `user_balance`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID（唯一）',
  `balance` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '用户当前余额',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户余额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_balance
-- ----------------------------
INSERT INTO `user_balance` VALUES (1, 125, 86937.27, '2025-09-23 21:12:07');
INSERT INTO `user_balance` VALUES (2, 128, 33.22, '2025-09-05 11:37:03');
INSERT INTO `user_balance` VALUES (3, 129, 1.00, '2025-08-23 15:40:11');
INSERT INTO `user_balance` VALUES (4, 130, 0.00, '2025-09-04 11:36:39');

SET FOREIGN_KEY_CHECKS = 1;
