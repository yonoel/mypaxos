package com.study.mypaxos.basic

import com.study.mypaxos.basic.message.AcceptRequest
import com.study.mypaxos.basic.message.PrepareRequest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
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
    fun sendPrepareRequest() {
        val prepareRequest = apiController.sendPrepareRequest()
        println(prepareRequest)
    }


    @Test
    fun prepareReply() {
        val prepareRequest = PrepareRequest("a", "20125451214541")
        apiController.prepareReply(prepareRequest)
        assertThrows(DomainException::class.java) {
            apiController.prepareReply(prepareRequest)

        }
    }


    @Test
    fun acceptReply() {
        assertThrows<DomainException> {
            apiController.acceptReply(AcceptRequest("a", "12345", "abc"))
        }
    }

    @Test
    fun acceptReply2() {
        apiController.paxosValue = "va"
        apiController.lastProposer = PrepareRequest("a", "123456")
        apiController.acceptReply(AcceptRequest("a", "12345", "abc"))
    }

    @Test
    fun acceptReply3() {
        assertThrows<DomainException> {
            apiController.lastProposer = PrepareRequest("a", "123456")
            apiController.acceptReply(AcceptRequest("a", "12345", "abc"))
        }

    }
}