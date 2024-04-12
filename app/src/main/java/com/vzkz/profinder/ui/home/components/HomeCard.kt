package com.vzkz.profinder.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer

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
        Box(
            modifier = boxModifier
                .weight(8f)
                .fillMaxSize()
                .padding(cardPadding)
                .shadow(1.dp, shape = MaterialTheme.shapes.medium)
                .background(cardColor)
                .padding(contentPadding)
        ) {
            val alignment = if (placeRight) Alignment.TopEnd else Alignment.TopStart
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .align(alignment),
                text = title,
                color = contentColor,
                fontFamily = fontFamily,
            )
            MyColumn(modifier = Modifier.align(Alignment.TopCenter)) {
                MySpacer(size = 40)
                content()
            }
            if (placeRight && !isEditFavListEmpty) {
                IconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    onClick = { onEditFavList() }) {
                    Icon(
                        imageVector = if (editFavList) Icons.Filled.EditOff else Icons.Filled.Edit,
                        contentDescription = "Edit fav list"
                    )
                }
            }
        }
        if (!placeRight) Spacer(modifier = Modifier.weight(1f))
    }
}

