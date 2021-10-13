package com.study.mypaxos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

/**
 *  提供给客户端调用的api
 * @author yonoel
 * @version 2021/10/11
 */
@RestController
@RequestMapping("/api")
class ApiController {


    @Autowired
    private lateinit var webClient: WebClient

    @Autowired
    private lateinit var environment: Environment

    @Autowired
    private lateinit var self: ServerConfigDTO

    @Autowired
    private lateinit var context: ApplicationContext

    @Autowired
    private lateinit var clusterConfig: ClusterConfig


    private var paxosValue: String? = null

    @RequestMapping(method = [RequestMethod.GET], path = ["client-call"])
    fun beCalled(@RequestParam param: String): String {
        when (param) {
            "init" -> {
                // 初始化集群,假设已经知道5个节点
                val clusterConfigDTO = doHeartBeatAndGetClusterConfigDTO(self)
                saveClusterConfig(clusterConfigDTO)
            }
            "setValue" -> {
                if (paxosValue != null) {
                    return paxosValue as String
                } else {
                    val prepareReply = sendPrepareRequest()
                    handlePrepareReply(prepareReply)
                }
            }
        }
        return "ok"
    }

    @RequestMapping(method=[RequestMethod.GET],path=["prepare"])
    fun prepareReply(): String{
        return "ok"
    }



    // 向集群的大多数发起prepare请求
    private fun sendPrepareRequest(): List<Mono<PrepareReply>> {
        TODO("Not yet implemented")
        // 节点ID，proposerId
        val prepareRequest = PrepareRequest(clusterConfig.serverId!!, System.nanoTime().toString())
        return clusterConfig.nodes.map {
            webClient.post()
                .uri("$it/prepare")
                .body<PrepareRequest>(prepareRequest)
                .retrieve()
                .bodyToMono<PrepareReply>()
        }
    }

    /**
     * 处理请求，取没有忽略的响应数量，若为大多数，准备发起accept请求,否则，等待accept请求发给自己
     */
    private fun handlePrepareReply(prepareReply: List<Mono<PrepareReply>>) {
        TODO("Not yet implemented")
        prepareReply.map { it.doOnSuccess {
        // 判定有没有返回允许success
             Mono.just(it.code == "ok")
        } }
    }

    /**
     * 保持集群配置->就知道这个集群的大多数节点该是多少
     */
    private fun saveClusterConfig(clusterConfigDTO: ClusterConfig) {
        TODO("Not yet implemented")
    }

    /**
     * 初始化心跳
     */
    private fun doHeartBeatAndGetClusterConfigDTO(self: ServerConfigDTO): ClusterConfig {
        TODO("not implemented")
        val property = environment.getProperty("cluster.nodes").orEmpty()
        val selfAddress = environment.getProperty("server.address")!!
        val nodes = property.split(".").toList()
    }
}