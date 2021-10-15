package com.study.mypaxos

import com.squareup.moshi.Moshi
import com.study.mypaxos.message.AcceptRequest
import com.study.mypaxos.message.ClusterConfig
import com.study.mypaxos.message.PrepareRequest
import com.study.mypaxos.message.ServerConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

/**
 *  提供给客户端调用的api
 * @author yonoel
 * @version 2021/10/11
 */
@RestController
@RequestMapping("/api")
class ApiController {


    //    @Autowired
//    private lateinit var webClient: WebClient
    @Autowired
    private lateinit var template: RestTemplate

    @Autowired
    private lateinit var environment: Environment

//    @Autowired
//    private lateinit var self: ServerConfigDTO

    @Autowired
    private lateinit var context: ApplicationContext

    @Autowired
    private lateinit var clusterConfig: ClusterConfig

    /**
     * 一致性值
     */
    var paxosValue: String? = null

    /**
     * 最近的proposer请求，
     */
    var lastProposer: PrepareRequest? = null

    @Autowired
    private lateinit var moshi: Moshi

    @RequestMapping(method = [RequestMethod.GET], path = ["client-call/{param}"])
    fun beCalled(@PathVariable param: String): String {
        when (param) {
            "init" -> {
                // 初始化集群,假设已经知道5个节点
//                val clusterConfigDTO = doHeartBeatAndGetClusterConfigDTO(self)
//                saveClusterConfig(clusterConfigDTO)
            }
            "setValue" -> {
                if (paxosValue != null) {
                    return paxosValue as String
                } else {
                    val prepareReply = sendPrepareRequest()
                    handlePrepareReply(prepareReply)
                    sendAcceptRequest()
                }
            }
        }
        return "ok"
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["prepare"])
    fun prepareReply(
        @RequestBody prepare: PrepareRequest
    ): String {
        // 若本身就有值，返回值
        if (paxosValue != null) {
            return moshi.adapter(AcceptRequest::class.java)
                .toJson(AcceptRequest(lastProposer!!.nodeId, lastProposer!!.proposerId, paxosValue!!))
        }
        // 没有值，那么校验有没有接受proposer
        lastProposer?.proposerId?.toLong()?.let { validateProposerMessage(it) }
        // 没有接受proposer，返回OK
        this.lastProposer = prepare
        return "ok"
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["accept"])
    fun acceptReply(
        @RequestBody accept: AcceptRequest
    ): String {
        // 若本身有值，发挥proposer以及值
        if (paxosValue != null) {
            return moshi.adapter(AcceptRequest::class.java)
                .toJson(AcceptRequest(lastProposer!!.nodeId, lastProposer!!.proposerId, paxosValue!!));
        }
        if (lastProposer == null) {
            throw DomainException("没有接收到到提议请求")
        }
        // 没有值，那么校验有没有接受该proposer
        lastProposer!!.compareProposer(accept)
        // 没有接受proposer，返回OK
        this.paxosValue = accept.value
        return "ok"
    }


    // 向集群的大多数发起prepare请求
    fun sendPrepareRequest(): List<ResponseEntity<String>> {
        val nanoTime = System.nanoTime()
        val proposerId = nanoTime.toString()

        validateProposerMessage(nanoTime)

        return clusterConfig.nodes.map {
            try {
                template.postForEntity(
                    "http://$it/prepare",
                    // 节点ID，proposerId
                    PrepareRequest(clusterConfig.serverId!!, proposerId),
                    String::class.java
                )
            } catch (e: Exception) {
                println(e)
                ResponseEntity.badRequest().build()
            }

        }
    }

    // 说明接到过proposer->那暂时不能接受该提议
    private fun validateProposerMessage(nanoTime: Long) {
        if (lastProposer?.isSelfBigThanInput(nanoTime) == false) {
            throw DomainException("已经接受过提议 ${moshi.adapter(PrepareRequest::class.java).toJsonValue(lastProposer)}")
        }
    }

    /**
     * 处理请求，取没有忽略的响应数量，若为大多数，准备发起accept请求,否则，等待accept请求发给自己
     */
    fun handlePrepareReply(prepareReply: List<ResponseEntity<String>>) {
        val successCount = prepareReply.count { it.body == "ok" }
        // 其实能在reply里找到可能存在的取值，终止accept。
        // 若大于大多数quorum，发起accept请求
        if (successCount < 5 / 2) {
            throw  DomainException("prepare请求失败: server-id:${clusterConfig.serverId},proposer:");
        }

    }

    /***
     * 发送accept请求
     */
    fun sendAcceptRequest(): List<ResponseEntity<String>> {
        val randomValue = "value:${System.nanoTime()}"
        return clusterConfig.nodes.map {
            try {
                template.postForEntity(
                    "http://$it/accept",
                    // 节点ID，proposerId,value
                    AcceptRequest(lastProposer!!.nodeId, lastProposer!!.proposerId, randomValue),
                    String::class.java
                )
            } catch (e: Exception) {
                println(e)
                ResponseEntity.badRequest().build()
            }

        }
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
    private fun doHeartBeatAndGetClusterConfigDTO(self: ServerConfig): ClusterConfig {
        TODO("not implemented")
        val property = environment.getProperty("cluster.nodes").orEmpty()
        val selfAddress = environment.getProperty("server.address")!!
        val nodes = property.split(".").toList()
    }
}