package com.vzkz.profinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

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
    placeRight: Boolean = false,
    content: @Composable () -> Unit
) {
    MyRow(modifier = modifier) {
        if (placeRight) Spacer(modifier = Modifier.weight(1f))
        MyColumn(
            modifier = boxModifier
                .weight(5f)
                .fillMaxSize()
                .padding(cardPadding)
                .shadow(1.dp, shape = MaterialTheme.shapes.medium)
                .background(cardColor)
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = title,
                color = contentColor,
                fontFamily = fontFamily,
                modifier = if (placeRight) Modifier.align(
                    Alignment.End
                ) else Modifier.align(Alignment.Start)
            )
            content()
        }
        if (!placeRight) Spacer(modifier = Modifier.weight(1f))
    }
}