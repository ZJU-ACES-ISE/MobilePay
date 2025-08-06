# MobilePay - 移动支付系统

## 📋 项目概述

MobilePay 是一个基于 Spring Boot 微服务架构的移动支付系统，提供完整的支付解决方案，包括用户管理、资产管理、支付处理、AI智能分析等功能。

## 🏗️ 系统架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Gateway       │    │   Admin         │    │   AppUser       │
│   (网关服务)     │    │   (管理后台)     │    │   (用户服务)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   AppAssets     │    │   AppPayment    │    │   AppAi         │
│   (资产管理)     │    │   (支付服务)     │    │   (AI分析)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📦 模块说明

### 🚪 Gateway (网关服务)
- **端口**: 8080
- **功能**: 统一入口、路由转发、认证授权、限流熔断
- **技术栈**: Spring Cloud Gateway, JWT, Redis

### 👤 AppUser (用户服务)
- **端口**: 8081
- **功能**: 用户注册登录、个人信息管理、实名认证
- **技术栈**: Spring Boot, MyBatis-Plus, MySQL

### 🏢 Admin (管理后台)
- **端口**: 8082
- **功能**: 管理员登录、系统配置、数据统计、策略管理
- **技术栈**: Spring Boot, MyBatis-Plus, MySQL

### 💰 AppAssets (资产管理)
- **端口**: 8083
- **功能**: 银行卡管理、余额查询、转账记录、账单管理
- **技术栈**: Spring Boot, MyBatis-Plus, MySQL

### 💳 AppPayment (支付服务)
- **端口**: 8084
- **功能**: 支付处理、收款码、公交地铁支付、优惠策略
- **技术栈**: Spring Boot, MyBatis-Plus, MySQL

### 🤖 AppAi (AI分析服务)
- **端口**: 8085
- **功能**: 智能消费分析、消费建议、异常支出提醒
- **技术栈**: Spring Boot, MyBatis-Plus, OpenAI/DeepSeek API

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 2.3.4
- **微服务**: Spring Cloud Hoxton.SR8
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.10.1
- **缓存**: Redis
- **认证**: JWT
- **API文档**: Swagger/OpenAPI

### 开发工具
- **JDK**: Java 17
- **构建工具**: Maven 3.8.1
- **IDE**: IntelliJ IDEA
- **数据库工具**: Navicat/MySQL Workbench

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 1. 克隆项目
```bash
git clone <repository-url>
cd MobilePay
```

### 2. 数据库初始化
```bash
# 创建数据库
CREATE DATABASE mobilepay CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# 执行SQL脚本
mysql -u root -p mobilepay < mobilepay.sql
```

### 3. 配置文件
复制各模块的配置文件模板：
```bash
cp gateway/src/main/resources/bootstrap.yml.template gateway/src/main/resources/bootstrap.yml
cp appUser/src/main/resources/application.yml.template appUser/src/main/resources/application.yml
cp admin/src/main/resources/application.yml.template admin/src/main/resources/application.yml
cp appAssets/src/main/resources/application.yml.template appAssets/src/main/resources/application.yml
cp appPayment/src/main/resources/application.yml.template appPayment/src/main/resources/application.yml
cp appAi/src/main/resources/application.yml.template appAi/src/main/resources/application.yml
```

### 4. 修改配置
根据你的环境修改各模块的配置文件：
- 数据库连接信息
- Redis连接信息
- 服务端口
- AI API密钥

### 5. 启动服务
```bash
# 启动网关服务
cd gateway && mvn spring-boot:run

# 启动用户服务
cd appUser && mvn spring-boot:run

# 启动管理后台
cd admin && mvn spring-boot:run

# 启动资产管理
cd appAssets && mvn spring-boot:run

# 启动支付服务
cd appPayment && mvn spring-boot:run

# 启动AI服务
cd appAi && mvn spring-boot:run
```

## 📊 数据库设计

### 核心表结构
- **user**: 用户基本信息
- **user_balance**: 用户余额
- **bank_card**: 银行卡信息
- **transfer_record**: 转账记录
- **receipt_code**: 收款码
- **receipt_transaction**: 收款交易
- **transit_record**: 公交地铁记录
- **admin**: 管理员信息
- **discount_strategy**: 优惠策略

## 🔧 API 接口

### 用户服务 (AppUser)
```http
POST /user/register          # 用户注册
POST /user/login            # 用户登录
GET  /user/profile          # 获取用户信息
PUT  /user/profile          # 更新用户信息
```

### 资产管理 (AppAssets)
```http
GET  /assets/balance         # 查询余额
GET  /cards/list            # 银行卡列表
POST /cards/add             # 添加银行卡
POST /transfer              # 转账
```

### 支付服务 (AppPayment)
```http
POST /payment/confirm       # 确认支付
POST /receipt/create        # 创建收款码
GET  /transit/fare          # 查询票价
POST /transit/pay           # 公交支付
```

### AI分析服务 (AppAi)
```http
POST /ai/ask                # AI分析请求
GET  /ai/models             # 获取可用模型
GET  /ai/strategies         # 获取可用策略
```

## 🤖 AI 智能分析

### 分析策略
1. **月度消费概览** - 分析用户月度消费情况
2. **消费分析与建议** - 提供个性化消费建议
3. **异常支出提醒** - 识别高频/异常消费

### 支持的AI模型
- **OpenAI** - GPT-3.5/GPT-4
- **DeepSeek** - DeepSeek Chat

### 使用示例
```json
POST /ai/ask
{
  "userId": 123,
  "modelType": "OPENAI",
  "strategyType": "MONTHLY_OVERVIEW",
  "timeRange": "2024-01"
}
```

## 🔐 安全特性

- **JWT认证**: 基于Token的身份验证
- **权限控制**: 基于角色的访问控制
- **数据加密**: 敏感信息加密存储
- **SQL注入防护**: MyBatis-Plus参数化查询
- **XSS防护**: 输入验证和输出编码

## 📈 性能优化

- **Redis缓存**: 热点数据缓存
- **数据库索引**: 优化查询性能
- **连接池**: 数据库连接池管理
- **异步处理**: 非阻塞操作
- **负载均衡**: 服务实例扩展

## 🧪 测试

### 单元测试
```bash
mvn test
```

### 集成测试
```bash
mvn verify
```

## 📝 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一异常处理
- 规范API响应格式

### 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目维护者: [Your Name]
- 邮箱: [your.email@example.com]
- 项目地址: [https://github.com/your-username/mobilepay]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

---

**注意**: 这是一个演示项目，生产环境使用前请确保进行充分的安全测试和性能优化。 