---

# QuantScope · 量化交易 API 开放平台

> QuantScope 是一个面向量化交易与金融分析场景的 **多租户 API 开放平台**。
> 提供统一 API 网关、AK/SK 签名认证、RBAC 权限控制、调用统计与微服务架构，支持高并发、可扩展的量化数据与指标服务。

---

## 📌 项目简介

在量化交易与金融分析系统中，交易指标、市场画像数据与情绪因子往往分散在不同系统中，
缺乏统一访问入口与安全控制机制。

QuantScope 致力于构建一个：

* 🔐 安全可靠（AK/SK + HMAC 签名认证）
* 🏗 微服务架构（高性能、可扩展）
* 🧱 多租户隔离（严格 tenant 级别隔离）
* 🛡 RBAC 权限模型（细粒度访问控制）
* 📊 调用统计与配额管理
* 🚀 面向开发者友好的 API SaaS 平台

---

## 🏗 系统整体架构

```text
Client / SDK
     |
     |  (AccessKey / Signature / Timestamp / Nonce)
     v
API Gateway (Spring Cloud Gateway)
     |
     |-- 身份认证
     |-- 签名校验
     |-- 租户隔离
     |-- RBAC 权限校验
     |-- 限流 / 防重放
     |-- 调用统计
     v
Dubbo 微服务
     |
     |-- 用户与 IAM 服务
     |-- 接口管理服务
     |-- 指标 / 量化服务
     |-- 计费服务（规划中）
     |
     v
MySQL / Redis / Kafka / Elasticsearch
```

---

## 🔐 安全模型设计

### 1️⃣ AK / SK 签名认证机制

为每个开发者分配：

* `AccessKey`
* `SecretKey`

签名规则：

* 使用 HMAC-SHA256
* 请求体 + SecretKey → 生成签名
* 携带 timestamp + nonce 防止重放攻击

### 签名验证流程

```text
客户端：
  body + secretKey → sign
  发送 accessKey / timestamp / nonce / sign

网关：
  校验 accessKey
  校验时间窗口
  校验 nonce 是否重复
  使用 SecretKey 重新计算签名
  比对签名
```

---

## 🧱 多租户架构设计

### 租户模型

* 每个企业 = 一个 Tenant
* 所有数据严格基于 tenant_id 隔离
* 不允许跨租户访问

```text
tenant
   ├── user
   ├── role
   ├── strategy
   ├── api_key
   └── quota
```

### 租户隔离策略

* JWT 内包含 tenantId
* 网关强制租户一致性校验
* 服务层自动拼接 tenant_id 查询条件
* 所有资源均 tenant-scoped

---

## 🛡 RBAC 权限模型

采用标准 Role-Based Access Control 模型：

```text
user_role → role_permission → permission
```

### 默认角色体系

* `tenant_owner`（主账号）
* `tenant_admin`
* `trader`
* `analyst`
* `viewer`

权限控制方式：

* AOP 注解拦截（`@RequirePermission`）
* 网关层预校验
* Redis 权限缓存优化

---

## 🚀 已实现功能

### 1️⃣ API 网关能力

* 基于 Spring Cloud Gateway（WebFlux）
* 全局过滤器实现统一请求入口
* 支持路由匹配与接口校验

---

### 2️⃣ 签名校验系统

* HMAC-SHA256
* 时间窗口校验
* Redis 存储 Nonce 防重放
* 服务端重新计算签名

---

### 3️⃣ API Key 管理

* AccessKey 唯一标识调用方
* SecretKey 不明文传输
* 支持密钥失效与后续轮换设计

---

### 4️⃣ 接口调用统计

* 按 用户 × 接口 维度统计
* 支持配额模型
* 为未来计费系统提供数据基础

---

### 5️⃣ Dubbo 微服务架构

* Dubbo 3.x + Nacos 注册中心
* 网关与后端完全解耦
* 高性能 RPC 通信

---

### 6️⃣ 接口路由与合法性校验

* 根据 Path + HTTP Method 匹配接口
* 非法接口在网关层直接拦截

---

## 📊 规划功能（Roadmap）

* 🔹 市场画像指标服务
* 🔹 情绪因子分析服务
* 🔹 Redis 分布式限流
* 🔹 Kafka 实时金融数据流
* 🔹 LLM 市场情绪分析集成
* 🔹 Elasticsearch 指标搜索能力
* 🔹 计费与套餐管理系统
* 🔹 分布式事务一致性保障
* 🔹 系统监控与可观测性

---

## 🧠 设计亮点

### ✅ 无状态认证（JWT）

* 无 Session 存储
* Token 校验 + 租户一致性强制验证

### ✅ 资源级授权模型

* 策略与 API 采用资源授权模式
* 主账号分配资源使用权限
* 子账号只能使用被授权资源

### ✅ 分层安全防御

1. 签名校验
2. 租户一致性校验
3. RBAC 权限验证
4. 限流与配额控制
5. 服务层 tenant 强制过滤

---

## 🧰 技术栈

* Java 17
* Spring Boot 3.x
* Spring Cloud Gateway
* Dubbo 3.x
* Nacos
* MySQL
* Redis
* Kafka（规划中）
* Elasticsearch（规划中）

---

## 📂 项目模块

```text
quant-scope-gateway        # API 网关
quant-scope-common         # 公共模型 / DTO
quant-scope-user-service   # 用户与 AK/SK 管理
quant-scope-interface      # 接口定义与管理
quant-scope-quant-service  # 量化指标服务
```

---

## 🎯 项目目标

* 构建可扩展的量化交易 API SaaS 平台
* 实现严格多租户隔离
* 提供安全高性能的指标接口服务
* 支持后续商业化计费模式


