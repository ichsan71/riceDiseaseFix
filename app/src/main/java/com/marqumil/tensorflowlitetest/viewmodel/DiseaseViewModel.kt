
package com.marqumil.tensorflowlitetest.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marqumil.tensorflowlitetest.DiseaseApplication
import com.marqumil.tensorflowlitetest.DiseaseUiState
import com.marqumil.tensorflowlitetest.data.DiseaseRepository
import com.marqumil.tensorflowlitetest.model.DiseaseRequestBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiseaseViewModel(private val diseaseRepository: DiseaseRepository) : ViewModel() {

    private val _diseaseUiState = MutableStateFlow<DiseaseUiState>(DiseaseUiState.Loading)
    val diseaseUiState: StateFlow<DiseaseUiState> = _diseaseUiState

    fun getDiseasePrediction(requestBody: DiseaseRequestBody) {
        viewModelScope.launch {
            _diseaseUiState.value = DiseaseUiState.Loading
            _diseaseUiState.value = try {
                val response = diseaseRepository.getDiseasePrediction(requestBody)
                Log.d("DiseaseViewModel", "Sending request: $requestBody")
                Log.d("DiseaseViewModel", "Received response: $response")
                DiseaseUiState.Success(response)
            } catch (e: IOException) {
                Log.e("DiseaseViewModel", "Error sending request: ${e.message}", e)
                DiseaseUiState.Error
            } catch (e: Exception) {
                Log.e("DiseaseViewModel", "Unexpected error: ${e.message}", e)
                DiseaseUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DiseaseApplication)
                val diseaseRepository = application.container.diseaseRepository
                DiseaseViewModel(diseaseRepository = diseaseRepository)
            }
        }
    }
}



