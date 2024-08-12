package com.marqumil.tensorflowlitetest


import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.marqumil.tensorflowlitetest.ui.theme.TensorflowLiteTestTheme
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marqumil.tensorflowlitetest.data.handlings
import com.marqumil.tensorflowlitetest.model.DiseasePredictionResponse
import com.marqumil.tensorflowlitetest.model.DiseaseRequestBody
import com.marqumil.tensorflowlitetest.viewmodel.DiseaseViewModel
import com.marqumil.tensorflowlitetest.viewmodel.ImagePickerViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TensorflowLiteTestTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImagePicker()
                }
            }
        }
    }
}

@Composable
fun ImagePicker(viewModel: ImagePickerViewModel = viewModel()) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val riceLeafResult by viewModel.riceLeafResult.collectAsState()
    val riceSpotColorResult by viewModel.riceSpotColorResult.collectAsState()
    val riceSpotModelResult by viewModel.riceSpotModelResult.collectAsState()

    val diseaseViewModel: DiseaseViewModel = viewModel(factory = DiseaseViewModel.Factory)
    val diseaseUiState by diseaseViewModel.diseaseUiState.collectAsState()

    var isResultsReady by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            photoUri = uri
            isResultsReady = false // Reset when a new image is selected
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                photoUri?.let { uri ->
                    bitmap = loadBitmapFromUri(uri, context)
                    isResultsReady = false // Reset when a new image is taken
                }
            }
        }
    )

    LaunchedEffect(photoUri) {
        photoUri?.let { uri ->
            bitmap = loadBitmapFromUri(uri, context)
        }
    }

    LaunchedEffect(bitmap) {
        bitmap?.let {
            viewModel.classifyRiceLeaf(it, context)
            viewModel.classifyRiceSpotColor(it, context)
            viewModel.classifyRiceSpotModel(it, context)
        }
    }

    // Track when all results are ready
    LaunchedEffect(riceLeafResult, riceSpotColorResult, riceSpotModelResult) {
        isResultsReady = riceLeafResult != null && riceSpotColorResult != null && riceSpotModelResult != null
    }

    // Trigger disease prediction when results are ready
    LaunchedEffect(isResultsReady) {
        if (isResultsReady) {
            val leafColorCode = when (riceLeafResult) {
                "Hijau kekuningan" -> "0"
                "Kuning" -> "1"
                else -> null
            }
            val spotColorCode = when (riceSpotColorResult) {
                "Abu kekuningan" -> "0"
                "Abu-abu" -> "1"
                "Tanpa warna bercak" -> "2"
                else -> null
            }
            val spotModelCode = when (riceSpotModelResult) {
                "Belah ketupat" -> "0"
                "Garis" -> "1"
                "Tanpa bercak" -> "2"
                else -> null
            }
            if (leafColorCode != null && spotColorCode != null && spotModelCode != null) {
                val requestBody = DiseaseRequestBody(
                    leafColor = leafColorCode,
                    spotColor = spotColorCode,
                    spotShape = spotModelCode
                )
                diseaseViewModel.getDiseasePrediction(requestBody)
            } else {
                diseaseViewModel.setDiseaseUiState(DiseaseUiState.Error)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxWidth()) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                ImageDisplay(bitmap)
                Spacer(modifier = Modifier.padding(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(id = R.string.upload),
                        style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }

            item {
                UploadButtons(
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = cameraLauncher,
                    context = context,
                    onPhotoUriCreated = { uri -> photoUri = uri }
                )
                Spacer(modifier = Modifier.padding(20.dp))
            }

            item {
                PredictionResults(
                    bitmap = bitmap,
                    riceLeafResult = riceLeafResult,
                    riceSpotColorResult = riceSpotColorResult,
                    riceSpotModelResult = riceSpotModelResult,
                    diseaseUiState = diseaseUiState
                )
            }
        }
    }
}



@Composable
fun ImageDisplay(bitmap: Bitmap?) {
    if (bitmap == null) {
        Image(
            painter = painterResource(id = R.drawable.img), // Ganti dengan ID gambar default Anda
            contentDescription = "Default Image",
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
        )
    } else {
        bitmap.let { bmp ->
            val safeBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true)
            Image(
                bitmap = safeBitmap.asImageBitmap(),
                contentDescription = "Image from the gallery",
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun UploadButtons(
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    context: Context,
    onPhotoUriCreated: (Uri?) -> Unit
) {
    Row {
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier
                .size(100.dp, 40.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = R.string.gallery),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Button(
            onClick = {
                val uri = createImageFile(context)
                onPhotoUriCreated(uri)
                uri?.let { cameraLauncher.launch(it) }
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = stringResource(id = R.string.camera))
        }
    }
}

@Composable
fun PredictionResults(
    bitmap: Bitmap?,
    riceLeafResult: String?,
    riceSpotColorResult: String?,
    riceSpotModelResult: String?,
    diseaseUiState: DiseaseUiState
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        if (bitmap == null) {
            Log.e("RiceResultErrorBitmap", "nothing")
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(id = R.string.prediction_result),
                    style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))
            riceLeafResult?.let { result ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        stringResource(id = R.string.leaf_color) + result,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }

            riceSpotColorResult?.let { result ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        stringResource(id = R.string.spot_color) + result,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }

            riceSpotModelResult?.let { result ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        stringResource(id = R.string.spot_shape) + result,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }

            when (diseaseUiState) {
                is DiseaseUiState.Loading -> {
                    Log.e("RiceDiseaseUiState", "Loading")
                    Text(text = "Loading...")
                }
                is DiseaseUiState.Success -> {
                    Log.e("RiceDiseaseUiState", "Success")
                    val disease = when (diseaseUiState.predictionResponse.prediction) {
                        "0" -> "Blast"
                        "1" -> "Hawar Daun Bakteri"
                        "2" -> "Normal"
                        "3" -> "Tungro"
                        else -> null
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            stringResource(id = R.string.disease) + (disease),
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            stringResource(id = R.string.todo),
                            style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    // Displaying handlings as bullet points
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        handlings.filter { it.name == disease }.forEach { handling ->
                            handling.description.forEach { item ->
                                BulletItem(text = item)
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                    }

                }
                is DiseaseUiState.Error -> {
                    Log.e("RiceDiseaseUiState", "Error")
                    LaunchedEffect(Unit) {
                        showDialog = true
                    }
                }
            }

            Spacer(modifier = Modifier.padding(20.dp))
        }

        if (showDialog) {
            AlertDialogExample(
                onConfirmation = { showDialog = false },
                onDismissRequest = { showDialog = false },
                icon = Icons.Default.Warning,
                dialogText = "Padi tidak terdeteksi, harap ambil ulang foto",
                dialogTitle = "Error"
            )
        }
    }
}



@Composable
fun BulletItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "•",
            style = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier.padding(end = 8.dp) // Spasi antara bullet dan teks
        )
        // Wrap the text to handle multi-line texts properly
        Text(
            text = text,
            style = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier
                .padding(start = 4.dp) // Padding untuk teks
                .weight(1f) // Take up remaining space to align text properly
        )
    }
}


@Composable
fun AlertDialogExample(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    icon: ImageVector,
    dialogText: String,
    dialogTitle: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = dialogTitle)
            }
        },
        text = {
            Text(dialogText)
        },
        confirmButton = {
            Button(
                onClick = onConfirmation
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Dismiss")
            }
        }
    )
}


private fun createImageFile(context: Context): Uri? {
    return try {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    } catch (ex: IOException) {
        Log.e("ImagePicker", "Error occurred while creating the file", ex)
        null
    }
}

private fun loadBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } catch (e: IOException) {
            Log.e("ImagePicker", "Failed to decode bitmap", e)
            null
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ImagePickerPreview() {
    val fakeBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    TensorflowLiteTestTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            ImagePickerPreviewContent(fakeBitmap)
        }
    }
}

@Composable
fun ImagePickerPreviewContent(fakeBitmap: Bitmap) {
    Column {
        ImageDisplay(fakeBitmap)
        Spacer(modifier = Modifier.padding(20.dp))
        PredictionResults(
            bitmap = fakeBitmap,
            riceLeafResult = "Hijau kekuningan",
            riceSpotColorResult = "Abu kekuningan",
            riceSpotModelResult = "Belah ketupat",
            diseaseUiState = DiseaseUiState.Success(
                DiseasePredictionResponse("0") // Example prediction
            )
        )
    }
}


