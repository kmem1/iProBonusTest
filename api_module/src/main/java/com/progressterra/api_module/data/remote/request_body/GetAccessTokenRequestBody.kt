package com.progressterra.api_module.data.remote.request_body

internal data class GetAccessTokenRequestBody(
    val idClient: String,
    val paramValue: String,
    val sourceQuery: Int = 0,
    val latitude: Int = 0,
    val longitude: Int = 0,
    val paramName: String = "device",
    val accessToken: String = ""
)