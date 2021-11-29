package com.progressterra.api_module.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object BonusesApi {

    private const val BASE_URL = "https://mp1.iprobonus.com"

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val bonusesApiService: BonusesApiService by lazy {
        retrofit().create(BonusesApiService::class.java)
    }
}