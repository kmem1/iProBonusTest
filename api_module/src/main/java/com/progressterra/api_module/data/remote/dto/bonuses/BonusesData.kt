package com.progressterra.api_module.data.remote.dto.bonuses

internal data class BonusesData(
    val currentQuantity: Double,
    val dateBurning: String,
    val forBurningQuantity: Double,
    val typeBonusName: String
)