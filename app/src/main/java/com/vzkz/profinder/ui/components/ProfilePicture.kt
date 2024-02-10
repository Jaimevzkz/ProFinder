package com.vzkz.profinder.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vzkz.profinder.R

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    profilePhoto: Uri?,
    size: Int
) {
    if (profilePhoto != null) {
        AsyncImage(
            modifier = modifier
                .size(size.dp)
                .clip(shape = CircleShape),
            model = profilePhoto,
            contentDescription = "Profile photo",
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            modifier = modifier
                .size(size.dp)
                .clip(shape = CircleShape),
            painter = painterResource(id = R.drawable.defaultprofile),
            contentDescription = "Profile photo",
            contentScale = ContentScale.Crop
        )
    }
}