package com.study.mypaxos.multi.message

import com.squareup.moshi.JsonClass
import com.study.mypaxos.DomainException

/**
 *
 * index指的是replicated-log里的index
 * @author yonoel
 * @version 2021/10/13
 */
@JsonClass(generateAdapter = true)
class PrepareRequest(val nodeId:String,val proposerId:String,val index:String) {

    fun isSelfBigThanInput(id:Long):Boolean = this.proposerId.toLong() > id
    fun compareProposer(accept: AcceptRequest) {
        if (nodeId != accept.nodeId || proposerId != accept.proposerId){
            throw DomainException("接受得不是该提议 accept:${accept.nodeId},${accept.proposerId}/proposer:${nodeId},${proposerId}")
        }
    }

}