package com.iman.bnpl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class BnplApplication

fun main(args: Array<String>) {
	runApplication<BnplApplication>(*args)
}
