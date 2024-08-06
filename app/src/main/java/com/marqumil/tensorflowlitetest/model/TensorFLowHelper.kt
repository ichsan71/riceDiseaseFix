package com.marqumil.tensorflowlitetest.model


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.marqumil.tensorflowlitetest.ml.Riceleafcolormodel
import com.marqumil.tensorflowlitetest.ml.Ricespotcolor
import com.marqumil.tensorflowlitetest.ml.Ricespotmodel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


object TensorFLowHelper {

    private const val imageSize = 300  // Image dimension as specified in the metadata
    private const val confidenceThreshold = 0.50f // Adjust this threshold based on your requirement

    private fun preprocessImage(image: Bitmap): ByteBuffer {
        // Ensure the bitmap is mutable and in ARGB_8888 format
        val convertedBitmap = image.copy(Bitmap.Config.ARGB_8888, true)

        val scaledBitmap = Bitmap.createScaledBitmap(convertedBitmap, imageSize, imageSize, false)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
        var pixel = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }
        return byteBuffer
    }

    fun classifyRiceLeaf(image: Bitmap, context: Context, callback: (result: String) -> Unit) {
        val model = Riceleafcolormodel.newInstance(context)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer = preprocessImage(image)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        Log.d("RiceLeafModel", "Confidences: ${confidences.joinToString(", ")}")

        val maxConfidence = confidences.maxOrNull() ?: 0f
        val maxPos = confidences.indices.maxByOrNull { confidences[it] } ?: -1
        val classes = arrayOf("Hijau kekuningan", "Kuning")

        if (maxConfidence < confidenceThreshold) {
            callback("Bukan padi")
        } else {
            callback(classes[maxPos])
        }

        model.close()
    }

    fun classifyRiceSpotColor(image: Bitmap, context: Context, callback: (result: String) -> Unit) {
        val model = Ricespotcolor.newInstance(context)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer = preprocessImage(image)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        Log.d("RiceSpotColorModel", "Confidences: ${confidences.joinToString(", ")}")

        val maxConfidence = confidences.maxOrNull() ?: 0f
        val maxPos = confidences.indices.maxByOrNull { confidences[it] } ?: -1
        val classes = arrayOf("Abu kekuningan", "Abu-abu", "Tanpa warna bercak")

        if (maxConfidence < confidenceThreshold) {
            callback("Bukan padi")
        } else {
            callback(classes[maxPos])
        }

        model.close()
    }

    fun classifyRiceSpotModel(image: Bitmap, context: Context, callback: (result: String) -> Unit) {
        val model = Ricespotmodel.newInstance(context)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer = preprocessImage(image)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        Log.d("RiceSpotModel", "Confidences: ${confidences.joinToString(", ")}")

        val maxConfidence = confidences.maxOrNull() ?: 0f
        val maxPos = confidences.indices.maxByOrNull { confidences[it] } ?: -1
        val classes = arrayOf("Belah ketupat", "Garis", "Tanpa bercak")

        if (maxConfidence < confidenceThreshold) {
            callback("Bukan padi")
        } else {
            callback(classes[maxPos])
        }

        model.close()
    }
}


