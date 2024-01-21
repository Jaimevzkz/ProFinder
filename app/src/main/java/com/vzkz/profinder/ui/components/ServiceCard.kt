package com.vzkz.profinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServiceCard(
    modifier: Modifier = Modifier,
    title: String,
    category: Categories,
    description: String,
    active: Boolean,
    onActivityChange: () -> Unit,
    onDelete: () -> Unit
) {
    MyColumn(
        modifier = modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        var editing by remember { mutableStateOf(false) }
        val paddingHorizontal = 24.dp
        var customPaddingHorizontal by remember { mutableStateOf(paddingHorizontal) }
        customPaddingHorizontal = if (editing) 0.dp else paddingHorizontal
        var expanded by remember { mutableStateOf(false) }

        MyRow(
            modifier = Modifier
                .padding(horizontal = customPaddingHorizontal)
                .padding(top = 12.dp)
        ) {
            if (editing) {
                IconButton(
                    onClick = { onDelete() }, modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        modifier = Modifier

                    )
                }
            }
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = category.name,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                editing = !editing
            }) {
                Icon(
                    imageVector = if (!editing) Icons.Outlined.Edit else Icons.Outlined.EditOff,
                    contentDescription = null
                )
            }
        }
        MySpacer(size = 8)
        Text(
            text = description,
            modifier = Modifier
                .padding(horizontal = paddingHorizontal)
                .padding(bottom = 24.dp)
                .clickable { expanded = !expanded },
            maxLines = if (expanded) Int.MAX_VALUE else 3
        )
        if (expanded) {
            Button(onClick = {
                expanded = false
                onActivityChange()
            }, modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = if (active) stringResource(R.string.set_as_inactive) else stringResource(
                        R.string.set_as_active
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ServiceCardPreview() {
    ProFinderTheme {
        ServiceCard(
            title = "Plumbing",
            category = Categories.Household,
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
                4
            ),
            active = true,
            onActivityChange = {},
            onDelete = {}
        )
    }
}