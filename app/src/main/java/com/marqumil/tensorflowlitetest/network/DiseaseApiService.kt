package com.marqumil.tensorflowlitetest.network


import com.marqumil.tensorflowlitetest.model.DiseasePredictionResponse
import com.marqumil.tensorflowlitetest.model.DiseaseRequestBody
import retrofit2.http.Body
import retrofit2.http.POST


interface DiseaseApiService {

    @POST("/predict")
    suspend fun getDiseasePrediction(
        @Body requestBody: DiseaseRequestBody
    ): DiseasePredictionResponse

}
