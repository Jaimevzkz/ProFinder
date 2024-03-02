package com.vzkz.profinder.ui.home.components.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer

@Composable
fun FavListShimmer(modifier: Modifier = Modifier) {
    MyColumn(modifier = modifier) {
        MySpacer(size = 12)
        ShimmeredFavListItem()
        MySpacer(size = 12)
        ShimmeredFavListItem()
        MySpacer(size = 12)
        ShimmeredFavListItem()

    }
}

@Composable
fun ShimmeredFavListItem() {
    MyRow {
        Box(
            modifier = Modifier
                .shimmer()
                .size(50.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(Color.Gray)
        )
        MySpacer(size = 12)
        MyColumn {
            Box(
                modifier = Modifier
                    .shimmer()
                    .height(20.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            MySpacer(size = 4)
            Box(
                modifier = Modifier
                    .shimmer()
                    .height(20.dp)
                    .padding(horizontal = 36.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
        }
    }
}