package com.study.mypaxos

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 * @author yonoel
 * @version 2021/10/14
 */
internal class ApiControllerTest : MypaxosApplicationTests() {

    @Autowired
    private lateinit var apiController: ApiController

    @Test
    fun beCalled() {
        apiController.beCalled("prepare")
    }

    @Test
    fun prepareReply() {
        val prepareRequest = PrepareRequest("a", "20125451214541")
        apiController.prepareReply(prepareRequest)
        assertThrows  (DomainException::class.java){
            apiController.prepareReply(prepareRequest)

        }
    }

    @Test
    fun sendPrepareRequest() {
        val prepareRequest = apiController.sendPrepareRequest()
        println(prepareRequest)
    }
}