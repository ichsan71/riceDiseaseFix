package com.marqumil.tensorflowlitetest.data

import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.marqumil.tensorflowlitetest.network.DiseaseApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType


interface  AppContainer {
    val diseaseRepository: DiseaseRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl =
        "http://sankamil.unikomcodelabs.id"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: DiseaseApiService by lazy {
        retrofit.create(DiseaseApiService::class.java)
    }

    override val diseaseRepository: DiseaseRepository by lazy {
        NetworkDiseaseRepository(retrofitService)
    }

}