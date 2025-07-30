package com.financial.alerts.checker.service

import com.financial.alerts.checker.dto.TransactionEvent
import org.springframework.stereotype.Service

@Service
class BudgetService {
    private val categoryBudgets = mapOf(
        "Food" to 500,
        "Transport" to 300,
        "Entertainment" to 200
    )

    private val userSpending = mutableMapOf<String, Int>()

    fun processSpending(event: TransactionEvent) {
        val key = "${event.profitAndLoseId}:${event.name}"
        val totalSpent =  userSpending.getOrDefault(key, 0) + event.amount
        userSpending[key] = totalSpent

        val limit = categoryBudgets[event.name] ?: Int.MAX_VALUE
        if (totalSpent > limit) {
            println("⚠\uFE0F ALERT: Depasit buget la ${event.name}: $totalSpent RON > $limit RON")
            //todo call a producer or sent to OpenAI
        } else {
            println("✅ Cheltuieli OK la ${event.name}: $totalSpent RON / $limit RON")
        }
    }
}