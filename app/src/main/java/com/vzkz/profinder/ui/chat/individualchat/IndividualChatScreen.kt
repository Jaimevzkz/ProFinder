package com.vzkz.profinder.ui.chat.individualchat

import android.content.res.Configuration
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.R
import com.vzkz.profinder.core.INDIVIDUALCHATITEMFORTEST
import com.vzkz.profinder.destinations.ChatScreenDestination
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.ReadStatus
import com.vzkz.profinder.ui.UiText
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
    otherNickname: String,
    otherProfilePhoto: Uri?,
    otherUid: String,
    combinedUid: String,
    individualChatViewModel: IndividualChatViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        individualChatViewModel.onInit(
            otherUid = otherUid,
            combinedUid = combinedUid
        )
    }

    val error = individualChatViewModel.state.error
    val chatList = individualChatViewModel.state.chatList
    val unreadMsgs = individualChatViewModel.state.unreadMsgNumber
    LaunchedEffect(chatList) {
        if (chatList.isNotEmpty() && !chatList.last().isMine)
            individualChatViewModel.markAsRead(
                combinedUid = combinedUid
            )
    }
    ScreenBody(
        messageList = chatList,
        unreadMagNumber = unreadMsgs,
        nickname = otherNickname,
        profilePhoto = otherProfilePhoto,
        onSendMessage = { message ->
            individualChatViewModel.sendMessage(
                combinedUid = combinedUid,
                otherUid = otherUid,
                otherNickname = otherNickname,
                otherProfilePicture = otherProfilePhoto,
                msg = message
            )
        },
        onFormatTime = { individualChatViewModel.getFormattedTime(it) },
        onGetDate = { individualChatViewModel.getDate(it) },
        error = error,
        onBackClicked = {
            navigator.navigate(ChatScreenDestination)
        },
        onCloseDialog = {
            individualChatViewModel.onCloseDialog()
        }
    )
}

@Composable
private fun ScreenBody(
    nickname: String,
    profilePhoto: Uri?,
    messageList: List<ChatMsgModel>,
    unreadMagNumber: Int,
    onSendMessage: (String) -> Unit,
    onFormatTime: (Long) -> String,
    onGetDate: (Long) -> String,
    onBackClicked: () -> Unit,
    error: UiText?,
    onCloseDialog: () -> Unit,
) {
    var message by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()


    Scaffold(
        topBar = {
            MyColumn {
                MyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Nav Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    MySpacer(size = 16)
                    ProfilePicture(
                        profilePhoto = profilePhoto,
                        size = 50,
                        shape = MaterialTheme.shapes.large
                    )
                    MySpacer(size = 8)
                    Text(
                        text = nickname,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                MySpacer(size = 8)
                HorizontalDivider()
                MySpacer(size = 8)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            MyColumn(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = scrollState,
                ) {
                    items(messageList) { message ->
                        var showDate = true
                        val currentIndex = messageList.indexOf(message)
                        if (currentIndex > 0) {
                            showDate = onGetDate(messageList[currentIndex].timestamp) != onGetDate(
                                messageList[currentIndex - 1].timestamp
                            )
                        }
                        if (showDate)
                            MyRow {
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .shadow(
                                            elevation = 1.dp,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = onGetDate(message.timestamp),
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        val readState =
                            if (messageList.indexOf(message) < (messageList.size) - unreadMagNumber) ReadStatus.Read else ReadStatus.Unread

                        ChatMessage(
                            chatMsgModel = message,
                            time = onFormatTime(message.timestamp),
                            ownMsg = message.isMine,
                            readStatus = readState
                        )
                    }
                }

                MyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    MyGenericTextField(
                        modifier = Modifier
                            .weight(1f),
                        hint = "Type your message...",
                        text = message,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        onTextChanged = { message = it },
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = OutlinedTextFieldDefaults.colors()
                            .copy(focusedIndicatorColor = MaterialTheme.colorScheme.primary),
                    )

                    IconButton(
                        onClick = {
                            if (message.isNotEmpty())
                                onSendMessage(message)
                            message = ""
                        }, modifier = Modifier
                            .padding(8.dp)
                            .size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
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

            LaunchedEffect(key1 = messageList, key2 = message) {
                if (messageList.isNotEmpty())
                    scrollState.scrollToItem(messageList.size - 1)
            }

            if(error != null){
                MyAlertDialog(
                    title = stringResource(R.string.error),
                    text = error.asString(),
                    onDismiss = { onCloseDialog() },
                    onConfirm = { onCloseDialog() },
                )
            }
        }

    }
}

@Composable
private fun ChatMessage(
    modifier: Modifier = Modifier,
    ownMsg: Boolean,
    chatMsgModel: ChatMsgModel,
    time: String,
    readStatus: ReadStatus
) {
    val genPadding = 12.dp
    val genPaddingMsgs = 56.dp
    val paddingMessages = if(ownMsg) PaddingValues(start = genPaddingMsgs) else PaddingValues(end = genPaddingMsgs)
    MyRow(
        modifier = modifier
            .padding(horizontal = genPadding, vertical = 4.dp)
            .padding(paddingMessages),
        horizontalArrangement = if (ownMsg) Arrangement.End else Arrangement.Start
    ) {
        if(ownMsg)
            Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .shadow(elevation = 1.dp, shape = MaterialTheme.shapes.large)
                .background(if (ownMsg) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                .padding(8.dp)
        ) {
            MyRow(modifier = if(ownMsg) Modifier.padding(end = 68.dp) else  Modifier.padding(end = 52.dp)) {
                Text(
                    text = chatMsgModel.msg,
                    color = if (ownMsg) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
            MyRow(modifier = Modifier.align(Alignment.BottomEnd)) {
                MyRow {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.align(Alignment.Bottom),/*.weight(2f)*/
                        color = if (ownMsg) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                    )
                    if (ownMsg) {
                        MySpacer(size = 2)

                        Icon(
                            imageVector = readStatus.icon,
                            contentDescription = "read",
                            tint = readStatus.tint,
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .size(16.dp)/*.weight(0.8f)*/
                        )
                    }
                }
            }
        }
        if(!ownMsg)
            Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            messageList = INDIVIDUALCHATITEMFORTEST,
            nickname = "larbyysbarber",
            profilePhoto = null,
            onFormatTime = { _ -> "12:00 PM" },
            error = null,
            onBackClicked = {},
            onCloseDialog = {},
            onSendMessage = {},
            onGetDate = { _ -> "Today" },
            unreadMagNumber = 3
        )
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview2() {
    ProFinderTheme {
        ChatMessage(
            ownMsg = true,
            chatMsgModel = ChatMsgModel(
                msgId = "",
//                msg = "Why are long messages not working???",
                msg = "hello!",
                timestamp = 3241341234,
                isMine = true
            ),
            time = "12:00 PM",
            readStatus = ReadStatus.Read
        )
    }

}
