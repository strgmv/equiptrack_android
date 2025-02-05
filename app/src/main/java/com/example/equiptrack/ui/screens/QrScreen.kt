package com.example.equiptrack.ui.screens

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun QrScreen(
    equipmentId: String
) {
    var dimen = 1000
    dimen = dimen * 3 / 4
    val inputValue = "equipment/$equipmentId"
    val bitmap = generateQr(inputValue, dimen)

    Surface {
        Column {
            if (bitmap == null) {
                Text(
                    text = "Unable to generate QR"
                )
            } else {
                Image(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = ""
                )
                Button(
                    onClick = {
                        saveQr(equipmentId, bitmap)
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "Save QR",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

fun generateQr(content: String, dimention: Int): Bitmap? {
    val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, dimention)
//    magic
    qrgEncoder.colorBlack = -1
    qrgEncoder.colorWhite = -16777216
    return try {
        qrgEncoder.bitmap
    } catch (_ : Exception) {
        null
    }
}

fun saveQr(id: String, bitmap: Bitmap) {
    CoroutineScope(Dispatchers.IO).launch {
        val imageFile = "$id.jpeg"
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            imageFile
        )
        val qrgSaver = QRGSaver()
        qrgSaver.save(
//            filePath.,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/EquipTrack/",
            id,
            bitmap,
            QRGContents.ImageType.IMAGE_JPEG
        )
        Log.d("r", "success")
    }
}