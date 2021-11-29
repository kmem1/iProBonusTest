package com.progressterra.api_module.data.remote.dto.bonuses

internal data class BonusesResultOperation(
    val codeResult: Int,
    val duration: Double,
    val idLog: String,
    val message: String,
    val messageDev: Any,
    val status: Int
)