package com.marqumil.tensorflowlitetest.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiseaseRequestBody(
    @SerialName("warna_daun") val leafColor: String,
    @SerialName("warna_bercak") val spotColor: String,
    @SerialName("bentuk_bercak") val spotShape: String
)

@Serializable
data class DiseasePredictionResponse(
    @SerialName("prediction") val prediction: String
)
