package com.vzkz.profinder.ui.services.components.userscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreenBody(
    modifier: Modifier = Modifier,
    onSeeList: () -> Unit
){
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMap {

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .padding(bottom = 20.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            onClick = { onSeeList() }
        ) {
            Icon(
                imageVector = Icons.Outlined.FormatListNumbered,
                contentDescription = "Show list view",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}