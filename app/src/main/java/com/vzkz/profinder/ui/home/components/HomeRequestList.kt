package com.vzkz.profinder.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.theme.acceptColor
import com.vzkz.profinder.ui.theme.rejectColor

@Composable
fun HomeRequestList(
    requestList: List<JobModel>,
    isUser: Boolean,
    onSeeProfile: (String) -> Unit,
    onAcceptRequest: (JobModel) -> Unit,
    onRejectRequest: (String, String) -> Unit
) {
    val fontColor = MaterialTheme.colorScheme.onPrimaryContainer
    if(requestList.isEmpty()){
        MyColumn(modifier = Modifier.fillMaxSize()){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =  "No pending requests...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1.7f))
        }
    } else{
        LazyColumn {
            items(requestList) { request ->
                MyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .shadow(1.dp, shape = MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(6.dp)
                ) {
                    MyColumn {
                        Text(
                            text = request.serviceName,
                            fontFamily = FontFamily(Font(R.font.oswald)),
                            fontSize = 22.sp,
                            color = fontColor
                        )
                        MyRow {
                            Text(
                                text = request.otherNickname,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp,
                                color = fontColor,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    onSeeProfile(request.otherUid)
                                }
                            )
                            MySpacer(size = 4)
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .shadow(1.dp, shape = CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                                    .padding(2.dp)
                            ) {
                                Text(
                                    text = request.price.toString() + stringResource(R.string.h),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (isUser) {
                        OutlinedButton(
                            onClick = { onRejectRequest(request.id, request.otherUid) },
                            border = BorderStroke(1.dp, fontColor),
                            modifier = Modifier.padding(end = 2.dp)
                        ) {
                            Text(text = "Cancel", color = fontColor, fontSize = 12.sp)
                        }
                    } else {
                        MyRow {
                            IconButton(
                                onClick = { onAcceptRequest(request) },
                                modifier = Modifier
                                    .shadow(elevation = 1.dp, shape = MaterialTheme.shapes.large)
                                    .height(36.dp)
                                    .width(52.dp)
                                    .background(acceptColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "accept request",
                                )
                            }
                            MySpacer(size = 8)
                            IconButton(
                                onClick = { onRejectRequest(request.id, request.otherUid) },
                                modifier = Modifier
                                    .shadow(elevation = 1.dp, shape = MaterialTheme.shapes.large)
                                    .height(36.dp)
                                    .width(52.dp)
                                    .background(rejectColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "reject request",
                                )
                            }
                        }
                    }
                }
                MySpacer(size = 2)
            }

        }

    }
}