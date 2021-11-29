package com.progressterra.api_module.data.remote

import com.progressterra.api_module.common.Constants
import com.progressterra.api_module.data.remote.dto.access_token.AccessTokenDto
import com.progressterra.api_module.data.remote.dto.bonuses.BonusesDto
import com.progressterra.api_module.data.remote.request_body.GetAccessTokenRequestBody
import retrofit2.http.*
import java.nio.channels.spi.AbstractSelectionKey

internal interface BonusesApiService {

    @POST(Constants.ACCESS_TOKEN_API_URL)
    suspend fun getAccessToken(
        @Header("AccessKey") accessKey: String,
        @Body body: GetAccessTokenRequestBody
    ): AccessTokenDto

    @GET(Constants.BONUSES_API_URL + "/{AccessToken}")
    suspend fun getBonuses(
        @Header("AccessKey") accessKey: String,
        @Path("AccessToken") accessToken: String
    ): BonusesDto
}