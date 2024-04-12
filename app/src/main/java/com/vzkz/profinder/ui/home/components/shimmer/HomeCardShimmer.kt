package com.vzkz.profinder.ui.home.components.shimmer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun HomeCardShimmer(
    modifier: Modifier = Modifier,
    placeRight: Boolean = false,
    cardColor: Color,
    cardPadding: PaddingValues,
    contentPadding: PaddingValues,
    content: @Composable () -> Unit = {}
){
    MyRow(modifier = modifier) {
        if (placeRight) Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .weight(6f)
                .fillMaxSize()
                .padding(cardPadding)
                .shadow(1.dp, shape = MaterialTheme.shapes.medium)
                .background(cardColor)
                .padding(contentPadding)
        ) {
            MyRow(
                modifier = if (placeRight) Modifier.align(
                    Alignment.TopEnd
                ) else Modifier.align(Alignment.TopStart)
            ) {
                if (placeRight) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Box(modifier = Modifier
                    .shimmer()
                    .padding(8.dp)
                    .height(20.dp)
                    .width(100.dp)
                    .background(Color.Gray)
                )
            }
            MyColumn (modifier = Modifier.align(Alignment.TopCenter)){
                MySpacer(size = 28)
                content()
            }
        }
        if (!placeRight) Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavListShimmerPreview() {
    ProFinderTheme {
        HomeCardShimmer(
            modifier = Modifier.height(200.dp),
            cardColor = MaterialTheme.colorScheme.surfaceVariant,
            cardPadding = PaddingValues(8.dp),
            contentPadding = PaddingValues(8.dp),
            placeRight = true
        )
    }
}