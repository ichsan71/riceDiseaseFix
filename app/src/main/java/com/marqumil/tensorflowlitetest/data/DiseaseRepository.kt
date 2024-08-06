package com.marqumil.tensorflowlitetest.data



import com.marqumil.tensorflowlitetest.model.DiseasePredictionResponse
import com.marqumil.tensorflowlitetest.model.DiseaseRequestBody
import com.marqumil.tensorflowlitetest.network.DiseaseApiService


interface DiseaseRepository {
    suspend fun getDiseasePrediction(requestBody: DiseaseRequestBody): DiseasePredictionResponse
}

class NetworkDiseaseRepository(
    private val diseaseApiService: DiseaseApiService
) : DiseaseRepository {
    override suspend fun getDiseasePrediction(requestBody: DiseaseRequestBody): DiseasePredictionResponse =
        diseaseApiService.getDiseasePrediction(requestBody)
}
