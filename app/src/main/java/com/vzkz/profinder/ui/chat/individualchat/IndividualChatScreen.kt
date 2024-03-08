package com.vzkz.profinder.ui.chat.individualchat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun IndividualChatScreen(
    navigator: DestinationsNavigator,
    tViewModel: IndividualChatViewModel = hiltViewModel()
) {
    val error = tViewModel.state.error
    ScreenBody(
        error = error,
        onCloseDialog = {
            tViewModel.onCloseDialog()
        }
    )
}

@Composable
private fun ScreenBody(
    error: UiError,
    onCloseDialog: () -> Unit,
) {
    var message by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        MyColumn(verticalArrangement = Arrangement.Top) {
            MyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Nav Back",
                    tint = MaterialTheme.colorScheme.primary
                )
                MySpacer(size = 16)
                ProfilePicture(profilePhoto = null, size = 50, shape = MaterialTheme.shapes.large)
                MySpacer(size = 8)
                Text(
                    "John Doe",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

            }
            MyColumn(modifier = Modifier.weight(1f)) {//todo make it lazy
                    var actualRandom: Int
                    var nextRandom: Int
                    actualRandom = (0..1).random()
                    for (i in 0..10) {
                        nextRandom = (0..1).random()

                        ChatMessage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            isLast = nextRandom != actualRandom,
                            ownMsg = actualRandom == 1
                        )
                        actualRandom = nextRandom
                    }
            }
            MyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                MyGenericTextField(
                    modifier = Modifier.weight(1f),
                    hint = "Type your message...",
                    text = message,
                    onTextChanged = { message = it },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = OutlinedTextFieldDefaults.colors()
                        .copy(focusedIndicatorColor = MaterialTheme.colorScheme.primary)
                )
                IconButton(
                    onClick = { /*TODO*/ }, modifier = Modifier
                        .padding(8.dp)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Send message",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                            .fillMaxSize()
                    )
                }
            }
        }


        MyAlertDialog(
            title = stringResource(R.string.error),
            text = error.errorMsg.orEmpty(),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = error.isError
        )
    }
}

@Composable
private fun ChatMessage(
    modifier: Modifier = Modifier,
    isLast: Boolean = false,
    ownMsg: Boolean = false,
    message: String = "Hello, how are you? I was wondering whether you could help me with something.",
    time: String = "03:34 PM",
    read: Int = 0, //-1 for not sent, 0 for unread, 1 for read
) {
    val genPadding = 12.dp
    val columnPadding =
        if (ownMsg) PaddingValues(start = 64.dp, end = genPadding)
        else PaddingValues(end = 64.dp, start = genPadding)

    MyColumn(
        modifier = modifier
            .padding(columnPadding)
            .padding(if (isLast) PaddingValues(bottom = genPadding) else PaddingValues(bottom = 8.dp))
    ) {
        Text(
            text = message,
            modifier = Modifier
                .shadow(elevation = 1.dp, shape = MaterialTheme.shapes.large)
                .background(if (ownMsg) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                .padding(8.dp)
        )
        if (isLast)
            Text(
                text = time,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(if (ownMsg) Alignment.End else Alignment.Start)
                    .padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            error = UiError(false, "Account wasn't created"),
            onCloseDialog = {},
        )
    }

}
