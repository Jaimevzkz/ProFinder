package com.vzkz.profinder.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.vzkz.profinder.R

@Composable
fun MyImageLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "App Logo",
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
    )
}