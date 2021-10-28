package com.study.mypaxos.multi.message

import com.squareup.moshi.JsonClass
import java.util.*

/**
 *
 * @author yonoel
 * @version 2021/10/27
 */
@JsonClass(generateAdapter = true)
class MultiPrepareReply(
    var nodeId: String, var proposerId: Int, val index: Int, var value: String?,
    var maxIndex: Int, var noMoreAccepted: Boolean, var isOk: Boolean
) {
    constructor(request: MultiPrepareRequest, log: ArrayList<LogEntry>?) : this(
        request.nodeId, request.proposerId, request.index, null, 0, true, true
    ) {
        // 取本地日志entry
        if (log != null) {
            val logEntry = log[request.index]

            // 若entry不为null且已经提交
            if (logEntry != null && logEntry.committed) {
                this.nodeId = logEntry.nodeId
                this.proposerId = logEntry.proposerId
                this.value = logEntry.value
            }
            // 表示没有收到accept请求，不需要重新选举
            this.noMoreAccepted = true
        } else {
            // 判定是否接受了别的log提议，若接受了则直接拒绝
            this.isOk = false
            return
        }



    }


}
