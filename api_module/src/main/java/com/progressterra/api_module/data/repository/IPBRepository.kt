package com.progressterra.api_module.data.repository

import com.progressterra.api_module.common.RequestResult
import com.progressterra.api_module.data.remote.BonusesApi
import com.progressterra.api_module.data.remote.request_body.GetAccessTokenRequestBody
import java.text.SimpleDateFormat
import java.util.*

class IPBRepository(private val API_KEY: String) {

    suspend fun getAccessToken(clientId: String, deviceId: String): RequestResult<String> {
        val requestBody = GetAccessTokenRequestBody(idClient = clientId, paramValue = deviceId)

        val accessTokenDto = BonusesApi.bonusesApiService.getAccessToken(API_KEY, requestBody)

        return if (accessTokenDto.result.status == 0) {
            RequestResult.success(accessTokenDto.accessToken)
        } else {
            RequestResult.failed(accessTokenDto.result.message)
        }
    }

    suspend fun getCurrentBonuses(accessToken: String): RequestResult<Double> {
        val bonusesDto = BonusesApi.bonusesApiService.getBonuses(API_KEY, accessToken)

        return if (bonusesDto.bonusesResultOperation.status == 0) {
            RequestResult.success(bonusesDto.bonusesData.currentQuantity)
        } else {
            RequestResult.failed(bonusesDto.bonusesResultOperation.message)
        }
    }

    suspend fun getBurningBonuses(accessToken: String): RequestResult<Double> {
        val bonusesDto = BonusesApi.bonusesApiService.getBonuses(API_KEY, accessToken)

        return if (bonusesDto.bonusesResultOperation.status == 0) {
            RequestResult.success(bonusesDto.bonusesData.forBurningQuantity)
        } else {
            RequestResult.failed(bonusesDto.bonusesResultOperation.message)
        }
    }

    suspend fun getBonusesBurningDate(accessToken: String): RequestResult<Date> {
        val bonusesDto = BonusesApi.bonusesApiService.getBonuses(API_KEY, accessToken)

        return if (bonusesDto.bonusesResultOperation.status == 0) {
            val date = SimpleDateFormat(
                "yyyy-MM-DDThh:mm:ss",
                Locale.getDefault()
            ).parse(bonusesDto.bonusesData.dateBurning)

            if (date != null) {
                RequestResult.success(date)
            } else {
                RequestResult.failed("Error while parsing date")
            }

        } else {
            RequestResult.failed(bonusesDto.bonusesResultOperation.message)
        }
    }
}