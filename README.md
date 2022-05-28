# KelvinMQ

## 简介

这是一个简单的消息队列实现，目前还在逐步开发中。参考的RabbitMQ的AMQP协议模型，同时做了很多简化工作。

消息队列KelvinMQ的简单实现，不包含鉴权/认证的功能，仅仅包含最核心的消息队列功能: 发布/订阅、连接管理、转发器管理和队列管理。

## 开发计划

- [x] DEMO - 模型设计
- [x] DEMO - 原型实现
- [x] DEMO - 心跳测试
- [x] MQ协议 - TCP传输
- [ ] MQ协议 - 命令设计
- [ ] 消息队列 - 服务端Context
- [ ] Context - 客户端 TCP连接 管理
- [ ] Context - 消息队列 Queue 管理
- [ ] Context - 消息转发器 Forwarder 管理
- [ ] 消息队列 - 命令异常处理

## 参考资料

- [Advanced Message Queuing Protocol - Protocol Specification](https://www.rabbitmq.com/resources/specs/amqp0-9-1.pdf) 