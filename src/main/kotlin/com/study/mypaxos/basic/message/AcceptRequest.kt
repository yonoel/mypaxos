package com.study.mypaxos.basic.message

import com.squareup.moshi.JsonClass

/**
 *
 * @author yonoel
 * @version 2021/10/15
 */
@JsonClass(generateAdapter = true)
class AcceptRequest(val nodeId:String,val proposerId:String,val value:String) {
}