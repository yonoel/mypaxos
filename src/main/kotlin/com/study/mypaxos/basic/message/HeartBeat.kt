package com.study.mypaxos.basic.message

/**
 *
 * @author yonoel
 * @version 2021/10/11
 */
class HeartBeat(val address:String, val serverId:String, val configIndex:Int) {
}