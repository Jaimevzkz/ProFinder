package com.vzkz.profinder.ui.components.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun IconShimmer(
    modifier: Modifier = Modifier,
    iconShimmerSize: Dp = 24.dp
) {
    Box( //Icon edit
        modifier = modifier
            .shimmer()
            .size(iconShimmerSize)
            .background(Color.Gray)
    )
}