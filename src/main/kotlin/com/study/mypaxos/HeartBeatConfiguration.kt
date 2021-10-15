package com.study.mypaxos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.security.SecureRandom
import javax.annotation.PostConstruct

/**
 *
 * @author yonoel
 * @version 2021/10/11
 */
@Configuration
class HeartBeatConfiguration {

    @Autowired
    private lateinit var environment: Environment

    @Bean
    fun initConfig(): ServerConfigDTO {
        //初始化服务器ID和配置ID
        val id = SecureRandom().nextInt(1 shl 24)
        var configId = 1
        return ServerConfigDTO("127.0.0.1:${environment.getProperty("server.port")}", id.toString(), configId)
    }

}