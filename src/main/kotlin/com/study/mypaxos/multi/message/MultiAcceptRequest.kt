package com.study.mypaxos.multi.message

import com.squareup.moshi.JsonClass

/**
 *
 * @author yonoel
 * @version 2021/10/15
 */
@JsonClass(generateAdapter = true)
 class MultiAcceptRequest(
    val nodeId: String,
    val proposerId: Int,
    val index: Int,
    val value: String,
    val firstUnchosenIndex: Int
)