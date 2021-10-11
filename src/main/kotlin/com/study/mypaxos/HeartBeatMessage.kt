package com.study.mypaxos

/**
 *
 * @author yonoel
 * @version 2021/10/11
 */
class HeartBeatMessage(val address:String,val serverId:String,val configIndex:Int) {
}