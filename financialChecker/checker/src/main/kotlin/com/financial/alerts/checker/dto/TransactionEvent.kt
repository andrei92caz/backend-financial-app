package com.financial.alerts.checker.dto

import com.financial.alerts.checker.enums.TransactionType

data class TransactionEvent(
    val id: Long,
    val profitAndLoseId: Long,
    val name: String,
    val amount: Int,
    val type: TransactionType
)
