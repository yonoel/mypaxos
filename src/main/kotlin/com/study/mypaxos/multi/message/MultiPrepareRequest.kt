package com.study.mypaxos.multi.message

import com.squareup.moshi.JsonClass
import com.study.mypaxos.DomainException
import com.study.mypaxos.basic.message.AcceptRequest

/**
 *
 * index指的是replicated-log里的index
 * @author yonoel
 * @version 2021/10/13
 */
@JsonClass(generateAdapter = true)
class MultiPrepareRequest(val nodeId: String, val proposerId: Int, val index: Int) {

    fun isSelfBigThanInput(id: Int): Boolean = this.proposerId > id



    private fun compare(nodeId: String,  proposerId: Int,  index: Int) {
        if (nodeId != this.nodeId || proposerId != this.proposerId || this.index != index) {
            throw DomainException("接受得不是该提议 input:${nodeId},${proposerId},${index},self-proposer:${this.nodeId},${this.proposerId},${this.index}")
        }
    }

    /**
     * 比较
     */
    fun compareProposer(accept: MultiPrepareReply) {
        compare(accept.nodeId,accept.proposerId,accept.index)
    }
}