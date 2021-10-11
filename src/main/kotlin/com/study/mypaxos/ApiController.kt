package com.study.mypaxos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

/**
 *  提供给客户端调用的api
 * @author yonoel
 * @version 2021/10/11
 */
@RestController
@RequestMapping("/api")
class ApiController {
    @Autowired
    private lateinit var webClient: WebClient

    @RequestMapping(method = [RequestMethod.GET], path = ["client-call"])
    fun String(@RequestParam param: String): String {
        if (param == "init"){
            // 初始化集群

        }
        return ""
    }
}