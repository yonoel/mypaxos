package com.study.mypaxos

import lombok.Data

/**
 *
 * @author yonoel
 * @version 2021/10/13
 */
@Data
class DomainException(val msg:String): Error() {
}