package com.study.mypaxos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient


@SpringBootApplication
class MypaxosApplication {
    fun main(args: Array<String>) {
        runApplication<MypaxosApplication>(*args)
    }


    @Bean
    @LoadBalanced
    fun loadBalancedWebClientBuilder(): WebClient {
        return WebClient.builder().build()
    }

}



