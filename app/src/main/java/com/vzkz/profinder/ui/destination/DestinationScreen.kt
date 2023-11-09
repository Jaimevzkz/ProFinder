package com.vzkz.profinder.ui.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun DestinationScreen() {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.error))
    
}