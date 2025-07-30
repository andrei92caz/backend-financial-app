package com.financial.alerts.checker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class CheckerApplication

fun main(args: Array<String>) {
	runApplication<CheckerApplication>(*args)
}
