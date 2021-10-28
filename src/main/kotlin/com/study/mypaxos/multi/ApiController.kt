package com.study.mypaxos.multi

import com.squareup.moshi.Moshi
import com.study.mypaxos.multi.message.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

/**
 *
 * @author yonoel
 * @version 2021/10/27
 */
@RestController
@RequestMapping("multi")
class ApiController {
    private val log: ArrayList<LogEntry> = ArrayList()

    @Autowired
    private lateinit var template: RestTemplate

    var firstUnchosenIndex: Int = 0

    /**
     * 提议ID
     */
    var proposerId: Int = 0

    @Autowired
    private lateinit var moshi: Moshi

    @Autowired
    private lateinit var clusterConfig: ClusterConfig

    var lastProposer: MultiPrepareRequest? = null
    var lastAccept: MultiAcceptRequest? = null

    init {
        // 初始化时可以是0，当若是连接到集群/或者故障回复后，需要修正proposerId还有index,
        lastProposer = MultiPrepareRequest(clusterConfig.serverId!!, 0, 0)
    }


    @RequestMapping(method = [RequestMethod.GET], path = ["client-call/{param}"])
    fun beCalled(@PathVariable param: String): String {
        choseRightIndex()
        sendAcceptRequest()
//        handlePrepareReply(prepareReply)
//        sendAcceptRequest()
        return "ok"
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["prepare"])
    fun prepareReply(
        @RequestBody prepare: MultiPrepareRequest
    ): MultiPrepareReply {
        // 判定是否接受了别的log提议，若接受了则直接拒绝
        if (this.lastProposer?.proposerId != prepare.proposerId || this.lastProposer?.nodeId != prepare.nodeId) {
            return MultiPrepareReply(prepare, null)
        }
        return MultiPrepareReply(prepare, log)
    }

    private fun getLogEntryByIndex(index: Int): LogEntry? {
        TODO("Not yet implemented")
    }

    /**
     * 选择正确的日志entry的index
     */
    private fun choseRightIndex() {
        var flag: Int
        var prepareResponse = arrayListOf<String>()
        do {
            var prepareResponse = sendPrepareRequest()
            flag = choseRightIndex(prepareResponse)
        } while (flag != 0)


    }

    /**
     * 发送prepare请求
     */
    private fun sendPrepareRequest(): List<ResponseEntity<MultiPrepareReply>> {

        val multiPrepareRequest =
            MultiPrepareRequest(clusterConfig.serverId!!, proposerId, firstUnchosenIndex)
        this.lastProposer = multiPrepareRequest
        // 这一步可以异步发送
        return clusterConfig.nodes.map {
            try {
                template.postForEntity(
                    "http://$it/multi/prepare",
                    multiPrepareRequest,
                    MultiPrepareReply::class.java
                )
            } catch (e: Exception) {
                println(e)
                ResponseEntity.badRequest().build()
            }

        }
    }


    /**
     * 处理prepare请求，若有值是选中的，则给自己的log加上这段entry
     */
    fun choseRightIndex(prepareReply: List<ResponseEntity<MultiPrepareReply>>): Int {
        // 需要换主，这里先写死自增proposerId
        if (ifNeedIncrementProposer(prepareReply)) {
            this.proposerId ++
            return 1
        }
        // 判定有没有请求返回值
        val firstOrNull = prepareReply.map { it.body }.firstOrNull { it!!.value != null }

        // 若有值说明已提交，修改自己的日志log,往下移动entry的index
        if (firstOrNull != null) {
            log[firstOrNull.index] = LogEntry(firstOrNull.index, firstOrNull.value!!, true, firstOrNull.proposerId)
            this.firstUnchosenIndex++
            // 继续发送prepare请求,执行这个逻辑找到可用的index
            return 1
        } else {
            // 若没有返回，说明可以发起accept请求
            return 0
        }

    }

    /**
     * 是否需要换主
     */
    private fun ifNeedIncrementProposer(prepareReply: List<ResponseEntity<MultiPrepareReply>>) :Boolean{
         return prepareReply.map { it.body }.firstOrNull { it!!.isOk == false } != null

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
                    MultiAcceptRequest(clusterConfig.serverId!!, proposerId, firstUnchosenIndex, randomValue, firstUnchosenIndex),
                    String::class.java
                )
            } catch (e: Exception) {
                println(e)
                ResponseEntity.badRequest().build()
            }

        }
    }


}