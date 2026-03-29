# llm-framework-quickstart

大模型开发框架选型对比入门

## 项目简介

本项目是基于Spring Boot的统一LLM调用框架，实现了通过标准OpenAI协议调用不同厂商的大模型。项目分别使用OpenAI
SDK、LangChain4j和Spring AI三种主流框架，实现了调用大模型标准OpenAI接口的流式和非流式方案。

### 核心技术选型对比

这是Java后端开发AI应用最核心的三个技术选型，我会从实现代码、原理、易用性、稳定性、社区、未来6大维度做深度对比，并给出企业级开发建议，全部基于最新稳定版（2026）。

#### 核心结论：

- **原生 OpenAI SDK**：最底层、最灵活、无封装损耗
- **LangChain4j**：全功能AI编排框架，适合复杂多步骤AI应用
- **Spring AI**：Spring生态原生集成，极简开发、适合Spring项目

## 工程目录结构

```
llm-framework-guide/
├── open-ai-protocols-boot/                 # 主应用模块
│   ├── src/main/java/com/llm/study/
│   │   ├── config/                         # 各框架配置类
│   │   ├── controller/                     # REST API控制器
│   │   ├── entity/                         # 实体类
│   │   ├── service/
│   │   │   ├── frames/                     # 各框架SDK实现服务
│   │   │   ├── providers/                  # 统一抽象服务提供者
│   │   │   │   ├── response/              # 非流式响应提供者
│   │   │   │   └── stream/                # 流式响应提供者
│   │   │   └── handlers/                  # 流式处理回调
│   │   └── utils/                          # 工具类
│   └── src/main/resources/
│       └── application.properties          # 配置文件
```

## 一、基础定义与核心定位

| 框架          | 定位           | 核心作用                        | 适用场景                       |
|-------------|--------------|-----------------------------|----------------------------|
| OpenAI SDK  | 官方原生客户端      | 直接调用OpenAI API（聊天、嵌入、函数调用等） | 简单调用、需要极致控制、无依赖            |
| LangChain4j | 全栈AI应用框架     | 大模型编排、RAG、工具调用、记忆、多模态       | 复杂AI助手、知识库、工作流             |
| Spring AI   | Spring生态AI集成 | 统一AI模型抽象、自动配置、Spring无缝对接    | Spring Boot项目、快速开发、标准化AI接口 |

## 二、框架原理对比

### 1. OpenAI SDK

- **原理**：HTTP客户端封装，直接映射OpenAI REST API
- **架构**：无抽象层 → 请求/响应1:1对应官方文档
- **优势**：无性能损耗、新功能第一时间支持
- **劣势**：无记忆、无RAG、无统一抽象，切换模型要改代码

### 2. LangChain4j

- **原理**：AI应用全生命周期编排框架（基于LangChain思想）
- **核心模块**：
    - 模型抽象（统一对接50+大模型）
    - 记忆管理（对话上下文）
    - RAG引擎（文档加载、分割、检索、生成）
    - 工具调用（函数调用、外部服务）
    - 执行链/代理（复杂AI工作流）
- **优势**：一站式解决所有AI应用需求
- **劣势**：学习成本高、封装重、简单场景显得冗余

### 3. Spring AI

- **原理**：Spring生态官方AI层，遵循Spring设计哲学
- **核心抽象**：
    - ChatClient/EmbeddingClient统一模型接口
    - 自动配置、依赖注入、事件机制
    - 与Spring Boot、Spring Cloud、Spring Data无缝融合
- **优势**：Spring开发者零学习成本
- **劣势**：功能比LangChain4j少，不适合极端复杂AI代理

## 三、核心维度深度对比

| 维度      | OpenAI SDK    | LangChain4j       | Spring AI           |
|---------|---------------|-------------------|---------------------|
| 易用性     | ⭐⭐（需手动处理所有逻辑） | ⭐⭐⭐⭐（开箱即用但学习成本高）  | ⭐⭐⭐⭐⭐（Spring开发者秒上手） |
| 开发效率    | ⭐⭐（重复代码多）     | ⭐⭐⭐⭐（复杂场景极快）      | ⭐⭐⭐⭐⭐（极简配置）         |
| 灵活性/控制力 | ⭐⭐⭐⭐⭐（完全原生）   | ⭐⭐⭐⭐（高度可扩展）       | ⭐⭐⭐（标准化，牺牲部分灵活性）    |
| 稳定性     | ⭐⭐⭐⭐⭐（官方客户端）  | ⭐⭐⭐（版本迭代快，偶有兼容问题） | ⭐⭐⭐⭐（Spring官方，稳定可靠） |
| 功能丰富度   | ⭐（仅API调用）     | ⭐⭐⭐⭐⭐（全功能AI框架）    | ⭐⭐⭐（满足80%通用场景）      |
| 跨模型支持   | ❌（仅OpenAI）    | ✅（50+主流模型）        | ✅（主流模型全覆盖）          |
| 社区支持    | ⭐⭐⭐⭐（官方）      | ⭐⭐⭐（活跃，生态中等）      | ⭐⭐⭐⭐⭐（Spring全球社区）   |
| 学习成本    | 低（会HTTP就会用）   | 高（AI概念+框架）        | 极低（会Spring就会用）      |
| 生产维护    | 简单（无依赖）       | 复杂（依赖多、版本管理）      | 简单（Spring标准化）       |

## 四、社区与未来发展

### 1. OpenAI SDK

- **维护方**：社区维护（官方推荐）
- **未来**：长期稳定存在，永远是底层基石，不会过时
- **更新**：跟随OpenAI最新API同步更新

### 2. LangChain4j

- **维护方**：独立开源团队
- **未来**：复杂AI应用首选，长期迭代
- **定位**：AI编排领域标准框架
- **风险**：迭代速度快，升级可能有breaking change

### 3. Spring AI

- **维护方**：VMware/Spring官方团队
- **未来**：Java AI开发事实标准，确定性最高
- **生态**：深度融合Spring全栈，企业级首选
- **趋势**：未来会成为Spring Boot标配模块

## 五、企业级实际开发建议（最关键）

### 1. 你应该选OpenAI SDK的场景

- 只需要简单调用OpenAI API，不需要复杂逻辑
- 需要完全控制请求/响应
- 项目无Spring、无第三方框架依赖
- 追求最小体积、最低性能损耗
- 用于SDK/基础组件封装

### 2. 你应该选LangChain4j的场景

- 做智能助手、RAG知识库、AI工作流、多轮对话
- 需要对接多种大模型（OpenAI+阿里+百度+本地模型）
- 复杂函数调用、工具链、代理Agent
- 非Spring项目（如Quarkus、纯Java）

### 3. 你应该选Spring AI的场景

- Spring Boot/Spring Cloud项目（90%企业场景）
- 追求开发速度、标准化、可维护性
- 通用AI功能：对话、嵌入、简单RAG、文件解析
- 企业生产环境：稳定性>极致功能
- 团队是Java Spring技术栈，不想学习新框架

## 六、API接口说明

### 启动服务

```bash
cd open-ai-protocols-boot
mvn spring-boot:run
```

服务默认启动在端口8080

### 非流式API

1. OpenAI SDK: `POST /api/openAI/chatResponse`
2. LangChain4j: `POST /api/langChain4j/chatResponse`
3. Spring AI: `POST /api/springAI/chatResponse`

### 流式API

1. OpenAI SDK: `POST /api/openAI/chatStream`
2. LangChain4j: `POST /api/langChain4j/chatStream`
3. Spring AI: `POST /api/springAI/chatStream`

### 请求示例

```json
{
  "query": "你好，请介绍一下自己",
  "temperature": 0.7
}
```

## 总结

- **OpenAI SDK**：底层原生，灵活但功能少
- **LangChain4j**：全功能AI编排，适合复杂应用
- **Spring AI**：Spring官方原生，企业开发首选，简单稳定

**一句话选型**：Spring项目用Spring AI，复杂AI用LangChain4j，极简调用用OpenAI SDK。

## API文档

项目集成Swagger UI，启动服务后可通过以下地址访问API文档：
http://localhost:8080/swagger-ui/index.html
