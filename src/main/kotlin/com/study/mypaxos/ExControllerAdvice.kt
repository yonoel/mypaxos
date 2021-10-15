package com.study.mypaxos

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 *
 * @author yonoel
 * @version 2021/10/15
 */
@RestControllerAdvice(basePackages = arrayOf("com.study.mypaxos"))
class ExControllerAdvice {

    @ExceptionHandler(DomainException::class)
    fun String(ex:DomainException){
        println(ex.message)
    }
}