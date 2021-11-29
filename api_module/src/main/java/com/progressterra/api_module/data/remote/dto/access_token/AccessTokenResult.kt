package com.progressterra.api_module.data.remote.dto.access_token

internal data class AccessTokenResult(
    val codeResult: Int,
    val duration: Double,
    val idLog: String,
    val message: String,
    val messageDev: Any,
    val status: Int
)