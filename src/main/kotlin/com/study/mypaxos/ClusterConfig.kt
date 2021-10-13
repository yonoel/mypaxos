package com.study.mypaxos

import lombok.Getter
import lombok.Setter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 *
 * @author yonoel
 * @version 2021/10/12
 */
@Configuration
@ConfigurationProperties(prefix = "paxos")
@Getter
@Setter
class ClusterConfig {
     var serverId: String? = null
     var nodes:List<String> = emptyList()

}