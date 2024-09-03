package com.iman.bnpl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BnplApplication

fun main(args: Array<String>) {
	runApplication<BnplApplication>(*args)
}
