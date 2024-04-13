package com.vzkz.profinder.ui.services.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.services.components.userscreen.ServiceCardShimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServicesScreenShimmer(
    modifier: Modifier = Modifier,
    cardBackgroundColor: Color
) {
    val scrollstate = rememberScrollState()
    MyColumn (modifier = modifier
        .padding(12.dp)
        .scrollable(scrollstate, orientation = Orientation.Vertical)) {

        MySpacer(size = 16)
        for (i in 0..5) {
            ServiceCardShimmer(backgroundColor = cardBackgroundColor)
            MySpacer(size = 16)
        }
    }
}




@Preview
@Composable
fun ServicesScreenShimmerPreview() {
    ProFinderTheme {
        ServicesScreenShimmer(cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant)
    }
}