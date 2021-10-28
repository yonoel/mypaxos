package com.study.mypaxos.multi.message

/**
 *
 * @author yonoel
 * @version 2021/10/27
 */

data class LogEntry(val index:Int,val value:String,val committed:Boolean,val proposerId:Int,val nodeId:String)