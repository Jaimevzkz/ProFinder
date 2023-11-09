package com.vzkz.profinder.core.boilerplate.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TScreen(navigator: DestinationsNavigator) {
    ScreenBody()
}

@Composable
private fun ScreenBody() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        //TODO
    }
}

@Preview
@Composable
fun SplashPreview() {
    ScreenBody()
}