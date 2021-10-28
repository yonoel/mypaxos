package com.study.mypaxos.multi.message

import com.squareup.moshi.JsonClass

/**
 *
 * @author yonoel
 * @version 2021/10/27
 */
@JsonClass(generateAdapter = true)
data class MultiAcceptResponse(val nodeId:String, val term:Int, val value:String,val firstUnchosenIndex:Int)