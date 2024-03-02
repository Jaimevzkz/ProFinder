package com.vzkz.profinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.SERVICEMODEL1FORTEST
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServiceCard(
    modifier: Modifier = Modifier,
    userCalling: Boolean,
    service: ServiceModel,
    backgroundColor: Color,
    fontColor: Color,
    onServiceInfo: () -> Unit = {},
    onActiveChange: () -> Unit ={},
    onDelete: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .shadow(1.dp, shape = MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .height(170.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        ProfilePicture(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart),
            profilePhoto = service.owner.profilePhoto,
            size = 60
        )

        if(userCalling){
            IconButton(
                onClick = { onServiceInfo() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) { //Show info
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Service info",
                    tint = fontColor
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) { //options Icon
                var expandedActorDropdownMenu by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { expandedActorDropdownMenu = true },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Options",
                        tint = fontColor
                    )
                }
                DropdownMenu(
                    modifier = Modifier,
                    expanded = expandedActorDropdownMenu,
                    onDismissRequest = { expandedActorDropdownMenu = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (service.isActive) "Set as inactive" else "Set as active",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            expandedActorDropdownMenu = false
                            onActiveChange()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (service.isActive) Icons.Filled.Remove else Icons.Filled.Add,
                                contentDescription = "Edit service"
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Delete", style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            expandedActorDropdownMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete service"
                            )
                        }
                    )
                }
            }
        }

        MyColumn {//Main content
            val fontSize = 24
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
                .padding(8.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp)
        ) {//Price
            Text(
                text = service.price.toString() + stringResource(R.string.h),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = fontColor
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ServiceCardPreview() {
    ProFinderTheme {
        ServiceCard(
            userCalling = false,
            service = SERVICEMODEL1FORTEST,
            onActiveChange = {},
            onDelete = {},
            onServiceInfo = {},
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            fontColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

