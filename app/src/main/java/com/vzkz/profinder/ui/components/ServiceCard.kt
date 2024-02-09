package com.vzkz.profinder.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.SERVICEMODEL1FORTEST
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServiceCard(
    modifier: Modifier = Modifier,
    service: ServiceModel,
    backgroundColor: Color,
    fontColor: Color,
    onActivityChange: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.large)
            .fillMaxWidth()
            .height(220.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        val pictureSize = 90.dp
        val picturePadding = 16.dp
        if (service.owner.profilePhoto != null) {
            Box(modifier = Modifier.align(Alignment.TopStart)) {
                AsyncImage(
                    modifier = Modifier
                        .size(pictureSize)
                        .padding(picturePadding)
                        .clip(shape = CircleShape)
//                    .align(Alignment.TopStart
                    ,
                    model = service.owner.profilePhoto,
                    contentDescription = "Profile photo",
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Image(
                modifier = Modifier
                    .size(pictureSize)
                    .clip(shape = CircleShape)
                    .align(Alignment.TopStart)
                    .padding(picturePadding),
                painter = painterResource(id = R.drawable.defaultprofile),
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Options",
                tint = fontColor
            )
        }
        MyColumn {
            val fontSize = 32
            Icon(
                imageVector = service.owner.profession?.icon ?: Icons.Outlined.Error,
                contentDescription = "Profession icon",
                tint = fontColor,
                modifier = Modifier
                    .size((fontSize + 4).dp)
            )
            Text(
                text = service.name,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = fontColor
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            Text(
                text = service.price.toString() + " $/h",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = fontColor
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Service info",
                tint = fontColor
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ServiceCardPreview() {
    ProFinderTheme {
        ServiceCard(
            service = SERVICEMODEL1FORTEST,
            onActivityChange = {},
            onDelete = {},
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            fontColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


/*
 MyColumn(
        modifier = modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.large)
            .background(backgroundColor)
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
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = fontColor)
            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = category.name,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                color = fontColor
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
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            color = fontColor
        )
        if (expanded) {
            Button(onClick = {
                expanded = false
                onActivityChange()
            }, modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = if (active) stringResource(R.string.set_as_inactive) else stringResource(
                        R.string.set_as_active
                    ), color = fontColor
                )
            }
        }
    }
*/