package com.vzkz.profinder.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    cardColor: Color,
    contentColor: Color,
    fontFamily: FontFamily,
    cardPadding: PaddingValues,
    contentPadding: PaddingValues,
    title: String,
    isEditFavListEmpty: Boolean = false,
    editFavList: Boolean = false,
    onEditFavList: () -> Unit = {},
    placeRight: Boolean = false,
    content: @Composable () -> Unit
) {
    MyRow(modifier = modifier) {
        if (placeRight) Spacer(modifier = Modifier.weight(1f))
        MyColumn(
            modifier = boxModifier
                .weight(6f)
                .fillMaxSize()
                .padding(cardPadding)
                .shadow(1.dp, shape = MaterialTheme.shapes.medium)
                .background(cardColor)
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top
        ) {
            MyRow(
                modifier = if (placeRight) Modifier.align(
                    Alignment.End
                ) else Modifier.align(Alignment.Start)
            ) {
                if (placeRight && !isEditFavListEmpty) {
                    IconButton(onClick = { onEditFavList() }) {
                        Icon(
                            imageVector = if (editFavList) Icons.Filled.EditOff else Icons.Filled.Edit,
                            contentDescription = "Edit fav list"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = title,
                    color = contentColor,
                    fontFamily = fontFamily,
                )

            }
            MySpacer(size = 2)
            content()
        }
        if (!placeRight) Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun FavListShimmer(
    modifier: Modifier = Modifier,
    placeRight: Boolean = false,
    cardColor: Color,
    cardPadding: PaddingValues,
    contentPadding: PaddingValues,
){
    MyRow(modifier = modifier) {
        if (placeRight) Spacer(modifier = Modifier.weight(1f))
        MyColumn(
            modifier = Modifier
                .weight(6f)
                .fillMaxSize()
                .padding(cardPadding)
                .shadow(1.dp, shape = MaterialTheme.shapes.medium)
                .background(cardColor)
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top
        ) {
            MyRow(
                modifier = if (placeRight) Modifier.align(
                    Alignment.End
                ) else Modifier.align(Alignment.Start)
            ) {
                if (placeRight) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = "title",
                )
                Box(modifier = Modifier
                    .shimmer()
                    .height(20.dp)
                    .width(80.dp)
                    .background(Color.Gray)
                )
            }
        }
        if (!placeRight) Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavListShimmerPreview() {
    ProFinderTheme {
        FavListShimmer(
            modifier = Modifier.height(200.dp),
            cardColor = MaterialTheme.colorScheme.surfaceVariant,
            cardPadding = PaddingValues(8.dp),
            contentPadding = PaddingValues(8.dp),
            placeRight = true
        )
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun FavListShimmerPreviewLight() {
    ProFinderTheme {
        FavListShimmer(
            modifier = Modifier.height(200.dp),
            cardColor = MaterialTheme.colorScheme.surfaceVariant,
            cardPadding = PaddingValues(8.dp),
            contentPadding = PaddingValues(8.dp),
            placeRight = true
        )
    }
}