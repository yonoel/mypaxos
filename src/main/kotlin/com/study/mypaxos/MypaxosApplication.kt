package com.study.mypaxos

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


@SpringBootApplication
class MypaxosApplication {




    //    @Bean
//    @LoadBalanced
//    fun loadBalancedWebClientBuilder(): WebClient {
//        return WebClient.builder().build()
//    }
    @Bean
    fun restTemplate() = RestTemplate()
    @Bean
    fun moshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

}

fun main(args: Array<String>) {
    runApplication<MypaxosApplication>(*args)
}



