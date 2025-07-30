package com.financial.alerts.checker.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.financial.alerts.checker.dto.TransactionEvent
import com.financial.alerts.checker.enums.TransactionType
import com.financial.alerts.checker.service.BudgetService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionEventConsumer(
    private val budgetService: BudgetService
) {
    private val objectMapper = jacksonObjectMapper()

    @KafkaListener(topics = ["transactions-topic"], groupId = "budget-alert-group")
    fun consume(message: String) {
        println("📥 Kafka listener triggered. Message: $message")
        val event = objectMapper.readValue<TransactionEvent>(message)
        println("🔍 Deserialized event: $event")
//        if (event.type == TransactionType.SPENDING) {
            budgetService.processSpending(event)
//        }
    }

}