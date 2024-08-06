package com.marqumil.tensorflowlitetest


import com.marqumil.tensorflowlitetest.model.DiseasePredictionResponse


sealed interface DiseaseUiState {
    data class Success(val predictionResponse: DiseasePredictionResponse) : DiseaseUiState
    data object Error : DiseaseUiState
    data object Loading : DiseaseUiState
}
