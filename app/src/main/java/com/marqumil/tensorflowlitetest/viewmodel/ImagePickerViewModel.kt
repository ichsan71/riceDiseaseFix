package com.marqumil.tensorflowlitetest.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marqumil.tensorflowlitetest.model.TensorFLowHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImagePickerViewModel : ViewModel() {

    private val _riceLeafResult = MutableStateFlow<String?>(null)
    val riceLeafResult: StateFlow<String?> = _riceLeafResult

    private val _riceSpotColorResult = MutableStateFlow<String?>(null)
    val riceSpotColorResult: StateFlow<String?> = _riceSpotColorResult

    private val _riceSpotModelResult = MutableStateFlow<String?>(null)
    val riceSpotModelResult: StateFlow<String?> = _riceSpotModelResult

    fun classifyRiceLeaf(image: Bitmap, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            TensorFLowHelper.classifyRiceLeaf(image, context) { result ->
                _riceLeafResult.value = result
            }
        }
    }

    fun classifyRiceSpotColor(image: Bitmap, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            TensorFLowHelper.classifyRiceSpotColor(image, context) { result ->
                _riceSpotColorResult.value = result
            }
        }
    }

    fun classifyRiceSpotModel(image: Bitmap, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            TensorFLowHelper.classifyRiceSpotModel(image, context) { result ->
                _riceSpotModelResult.value = result
            }
        }
    }
}



