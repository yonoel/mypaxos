# MyPaxos

自己实现的basic paxos算法

心跳超时时间: 60S
触发心跳: 100ms

项目启动时都是acceptor，通过心跳获取集群里的配置，并且持久化配置。

![img.png](img.png)

## 缺陷

1. paxos的服务节点需要知道quorum是多少？需要广播配置，或者一个中心来告诉配置。
2. 只有proposer知道哪个值被选中，所有响应accept的服务器的确也都有值，但是不知道该值有没有被选中
3. 若其他服务器想要明确知道被选中的值，需要基于序号执行paxos
4. 延迟高，rtt多，还有活锁问题

项目的一些文档

https://www.yuque.com/docs/share/61ee0856-f3c4-4549-bba9-49c5d639ceec?# 《Paxos lecture》
https://www.yuque.com/docs/share/32daedce-f2cc-4583-a2b1-f6ef4588a7b2?# 《The Paxos algorithm》