# MyPaxos

自己实现的basic paxos算法

心跳超时时间: 60S
触发心跳: 100ms

项目启动时都是acceptor，通过心跳获取集群里的配置，并且持久化配置。