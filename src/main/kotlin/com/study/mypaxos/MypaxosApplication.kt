package com.study.mypaxos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


@SpringBootApplication
class MypaxosApplication {

    fun main(args: Array<String>) {
        runApplication<MypaxosApplication>(*args)
    }


    //    @Bean
//    @LoadBalanced
//    fun loadBalancedWebClientBuilder(): WebClient {
//        return WebClient.builder().build()
//    }
    @Bean
    fun restTemplate() = RestTemplate()

}



