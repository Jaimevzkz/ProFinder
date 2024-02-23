package com.vzkz.profinder.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.vzkz.profinder.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


//Camera
@Composable
fun cameraIntent(
    uri: Uri?,
    onUploadImage: (Uri) -> Unit
) = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
    if (it && uri?.path?.isNotEmpty() == true) {
        onUploadImage(uri)
    }
}

@Composable
fun galleryIntent(onUploadImage: (Uri) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it?.path?.isNotEmpty() == true) {
            onUploadImage(it)
        }
    }

fun generateUri(userTitle: String, context: Context): Uri {
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.vzkz.profinder.provider",
        createFile(userTitle, context)
    )
}

fun createFile(userTitle: String, context: Context): File {
    val name: String =
        userTitle.ifEmpty { generateUniqueId() + "image" }
    return File.createTempFile(name, ".jpg", context.externalCacheDir)
}

fun generateUniqueId(): String {
    return SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
}

@Composable
fun UploadPhotoDialog(
    onDismiss: () -> Unit,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(shape = RoundedCornerShape(12)) {
            Column(modifier = Modifier.padding(24.dp)) {
                OutlinedButton(
                    onClick = {
                        onCameraClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(42)
                ) {
                    Text(stringResource(R.string.camera))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        onGalleryClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(42)
                ) {
                    Text(stringResource(R.string.from_gallery))
                }
            }
        }
    }
}