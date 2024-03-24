package com.vzkz.profinder.ui.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServicesScreenShimmer(
    modifier: Modifier = Modifier,
    cardBackgroundColor: Color
) {
    MyColumn (modifier = modifier.padding(12.dp)) {
//        MyRow {
//            Spacer(modifier = Modifier.weight(1f))
//            Box(
//                modifier = Modifier
//                    .shimmer()
//                    .height(24.dp)
//                    .width(100.dp)
//                    .background(Color.Gray)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Box(
//                modifier = Modifier
//                    .shimmer()
//                    .height(24.dp)
//                    .width(100.dp)
//                    .background(Color.Gray)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//        }
//        MySpacer(size = 40)
        MySpacer(size = 16)
        for (i in 0..2) {
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