package com.vzkz.profinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
            MyRow( modifier = if (placeRight) Modifier.align(
                Alignment.End
            ) else Modifier.align(Alignment.Start)) {
                if(placeRight){
                    IconButton(onClick = { onEditFavList() }) {
                        Icon(imageVector = if(editFavList) Icons.Filled.EditOff else Icons.Filled.Edit, contentDescription = "Edit fav list")
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