package com.vzkz.profinder.ui.components.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.vzkz.profinder.core.SERVICEMODEL1FORTEST
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServiceDetailsDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    service: ServiceModel,
    backgroundColor: Color,
    requestExists: Boolean,
    fontColor: Color,
    onSeeProfile: () -> Unit,
    onRequest: () -> Unit,
    onCancelRequest: (String) -> Unit,
    onCloseDialog: () -> Unit
) {
    if (isVisible) {
        Box(
            modifier = modifier
                .shadow(4.dp, shape = MaterialTheme.shapes.large)
                .fillMaxWidth()
                .height(524.dp)
                .background(backgroundColor),
        ) {
            MyColumn(modifier = Modifier.padding(12.dp)) {
                MyRow {
                    ProfilePicture(profilePhoto = service.owner.profilePhoto, size = 90)
                    Spacer(modifier = Modifier.weight(1f))
                    MyColumn(modifier = Modifier) {
                        Text(
                            text = service.owner.firstname,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(text = service.owner.nickname, fontSize = 16.sp)
                        MySpacer(size = 4)
                        OutlinedButton(
                            modifier = Modifier
                                .size(92.dp, 34.dp),
                            onClick = { onSeeProfile() },
                            contentPadding = ButtonDefaults.TextButtonContentPadding,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)

                        ) {
                            Text(text = "See profile", fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.weight(3f))

                }
                MyColumn {
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.displaySmall,
                        color = fontColor
                    )
                    MyRow {
                        Text(
                            text = service.category.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = fontColor,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp
                        )

                        Text(
                            text = service.price.toString() + stringResource(id = R.string.h),
                            style = MaterialTheme.typography.bodyLarge,
                            color = fontColor,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .shadow(1.dp, shape = CircleShape)
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(12.dp)
                        )
                    }
                }


                MySpacer(size = 8)
                Text(
                    text = service.servDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = fontColor
                )

            }
            OutlinedButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp),
                contentPadding = ButtonDefaults.TextButtonContentPadding,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                onClick = { if(requestExists) onCancelRequest(service.sid) else onRequest() }
            ) {
                Text(
                    text = if(requestExists) stringResource(R.string.cancel_request) else stringResource(
                        R.string.request
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fontSize = 16.sp
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onCloseDialog() }
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Close description")
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ServiceCardPreview() {
    ProFinderTheme {
        ServiceDetailsDialog(
            isVisible = true,
            service = SERVICEMODEL1FORTEST,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            fontColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onSeeProfile = {},
            onRequest = {},
            requestExists = true,
            onCloseDialog = {},
            onCancelRequest = {}
        )
    }
}