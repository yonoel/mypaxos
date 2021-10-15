package com.study.mypaxos

import com.squareup.moshi.JsonClass

/**
 *
 * @author yonoel
 * @version 2021/10/13
 */
@JsonClass(generateAdapter = true)
class PrepareRequest(val nodeId:String,val proposerId:String) {

    fun isSelfBigThanInput(id:Long):Boolean = this.proposerId.toLong() > id

}