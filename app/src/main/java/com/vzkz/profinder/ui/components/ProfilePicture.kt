package com.vzkz.profinder.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Success
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    profilePhoto: Uri?,
    size: Int
) {
    var loading by remember { mutableStateOf(true) }
    if (profilePhoto != null) {
        Box(modifier = Modifier, contentAlignment = Alignment.Center){
            AsyncImage(
                modifier = modifier
                    .size(size.dp)
                    .clip(shape = CircleShape),
                model = profilePhoto,
                onState = { state ->
                    if (state is Success) { loading = false }
                },
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop
            )
            if(loading){
                Box(
                    modifier = Modifier
                        .shimmer()
                        .size(size.dp)
                        .shadow(1.dp, shape = CircleShape)
                        .background(Color.Gray)
                )
            }
        }
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

@Composable
fun ProfilePictureShimmer(size: Int, modifier: Modifier = Modifier){
    Box( //Profile picture
        modifier = modifier
            .shimmer()
            .size(size.dp)
            .shadow(elevation = 1.dp, shape = CircleShape)
            .background(Color.Gray)
    )
}