package com.vzkz.profinder.ui.chat

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R
import com.vzkz.profinder.core.CHATLISTITEMFORTEST
import com.vzkz.profinder.destinations.ChatScreenDestination
import com.vzkz.profinder.destinations.IndividualChatScreenDestination
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.ProfilePictureShimmer
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ChatScreen(navigator: DestinationsNavigator, chatViewModel: ChatViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        chatViewModel.onInit()
    }
    val error = chatViewModel.state.error
    val chatList = chatViewModel.state.chatList
    val loading = chatViewModel.state.loading
    val uid = chatViewModel.state.uid
    ScreenBody(
        chatList = chatList,
        loading = loading,
        uid = uid,
        error = error,
        onChatClicked = { otherNickname, otherProfilePicture, otherUid->
            navigator.navigate(
                IndividualChatScreenDestination(
                    otherNickname = otherNickname,
                    otherProfilePhoto = otherProfilePicture,
                    otherUid = otherUid,
                    combinedUid = chatViewModel.combineUids(uid, otherUid)
                )
            )
        },
        onFormatTime = { chatViewModel.getFormattedTime(it) },
        onCloseDialog = {
            chatViewModel.onCloseDialog()
        },
        onBottomBarClicked = { navigator.navigate(it) }
    )
}

@Composable
private fun ScreenBody(
    chatList: List<ChatListItemModel>,
    loading: Boolean,
    uid: String,
    error: UiError,
    onChatClicked: (String, Uri?, String) -> Unit,
    onFormatTime: (Long) -> String,
    onCloseDialog: () -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    MyBottomBarScaffold(
        currentDestination = ChatScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            MyColumn(
                modifier = Modifier
                    .align(Alignment.TopCenter),
            ) {
                MyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        stringResource(id = R.string.chat),
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    if (isSearchVisible){
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                        TextField(
                            value = query,
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .width(220.dp),
                            onValueChange = { query = it },
                            colors = TextFieldDefaults.colors().copy(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                            ),
                        )

                    }

                    IconButton(onClick = {
                        isSearchVisible = !isSearchVisible
                        query = ""
                    }) {
                        Icon(
                            imageVector = if (!isSearchVisible) Icons.Filled.Search else Icons.Filled.Close,
                            contentDescription = "Search",
                        )
                    }
                }
                MySpacer(size = 8)
                HorizontalDivider(modifier = Modifier)
                MySpacer(size = 8)

                if (loading) {
                    for (i in 0..8)
                        ChatItemShimmer()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        items(chatList.filter {
                            it.nickname.contains(
                                other = query,
                                ignoreCase = true
                            )
                        }) { chatItem ->
                            ChatItem(
                                modifier = Modifier.clickable {
                                    onChatClicked(
                                        chatItem.nickname,
                                        chatItem.profilePhoto,
                                        chatItem.uid,
                                    )
                                },
                                uid = uid,
                                chatListItemModel = chatItem,
                                timeSent = onFormatTime(chatItem.timestamp)
                            )
                        }
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
}

@Composable
private fun ChatItem(
    modifier: Modifier = Modifier,
    uid: String,
    chatListItemModel: ChatListItemModel,
    timeSent: String
) {
    val nickname = chatListItemModel.nickname
    val isLastMsgMine =chatListItemModel.lastMsgUid == uid
    val lastMsg = if(isLastMsgMine) stringResource(R.string.you) + chatListItemModel.lastMsg else chatListItemModel.lastMsg
    MyRow(modifier = modifier.padding(vertical = 8.dp)) {
        ProfilePicture(
            profilePhoto = chatListItemModel.profilePhoto,
            size = 70,
            shape = MaterialTheme.shapes.large
        )
        MySpacer(size = 12)
        val maxNameLength = 14
        val maxMsgLength = 25
        Column {
            MyRow {
                Text(
                    text = if (nickname.length < maxNameLength) nickname
                    else nickname.substring(0, maxNameLength) + "...",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    timeSent,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            MyRow {
                Text(
                    if (lastMsg.length < maxMsgLength) lastMsg else lastMsg.substring(
                        0,
                        maxMsgLength
                    ) + "...",
                    color = if (chatListItemModel.unreadMsgNumber == 0 || isLastMsgMine) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                if (chatListItemModel.unreadMsgNumber > 0 && chatListItemModel.lastMsgUid != uid)
                    Text(
                        chatListItemModel.unreadMsgNumber.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(4.dp)
                            .shadow(elevation = 1.dp, shape = CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 6.dp)
                    )
            }
        }
    }
}

@Composable
private fun ChatItemShimmer() {
    MyRow(modifier = Modifier.padding(8.dp)) {
        ProfilePictureShimmer(70, shape = MaterialTheme.shapes.large)
        MySpacer(size = 12)
        Column {
            MyRow {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(140.dp)
                        .height(20.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(60.dp)
                        .height(20.dp)
                        .background(Color.Gray)
                )
            }
            MySpacer(size = 8)
            MyRow {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(220.dp)
                        .height(16.dp)
                        .background(Color.Gray)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        val chatList = mutableListOf(CHATLISTITEMFORTEST)
        for (i in 0..10) {
            chatList.add(
                if (i % 2 == 0) CHATLISTITEMFORTEST else CHATLISTITEMFORTEST.copy(
                    lastMsgUid = "123",
                    unreadMsgNumber = 0
                )
            )
        }
        ScreenBody(
            chatList = chatList,
            uid = "",
//            loading = true,
            loading = false,
            onFormatTime = { "12:00 P.M." },
            error = UiError(false, "Account wasn't created"),
            onCloseDialog = {},
            onBottomBarClicked = {},
            onChatClicked = { _, _, _ -> }
        )
    }
}

