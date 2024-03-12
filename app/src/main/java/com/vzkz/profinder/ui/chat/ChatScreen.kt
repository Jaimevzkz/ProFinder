package com.vzkz.profinder.ui.chat

import android.content.res.Configuration
import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.ChatScreenDestination
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ChatScreen(navigator: DestinationsNavigator, chatViewModel: ChatViewModel = hiltViewModel()) {
    val error = chatViewModel.state.error
    ScreenBody(
        error = error,
        onCloseDialog = {
            chatViewModel.onCloseDialog()
        },
        onBottomBarClicked = { navigator.navigate(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    error: UiError,
    onCloseDialog: () -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
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
                MyRow(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                ) {
                    Text(
                        stringResource(id = R.string.chat),
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
                MySpacer(size = 8)
                HorizontalDivider()
                MySpacer(size = 8)
                MyColumn (
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ){
                    for (i in 0..8) {
                        ChatItem()
                        MySpacer(size = 8)
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
    profilePicture: Uri? = null,
    read: Boolean = false,
    msgNumberUnread: Int = 2,
    name: String = "Larby Mubharak",
    lastMsg: String = "Hello, how are you? I was wondering whether you could help me with something.",
    timeSent: String = "03:34 PM"
) {
    MyRow(modifier = Modifier.padding(vertical = 8.dp)) {
        ProfilePicture(
            profilePhoto = profilePicture,
            size = 70,
            shape = MaterialTheme.shapes.large
        )
        MySpacer(size = 12)
        val maxNameLength = 14
        val maxMsgLength = 25
        Column {
            MyRow {
                Text(
                    if (name.length < maxNameLength) name else name.substring(
                        0,
                        maxNameLength
                    ) + "...",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(timeSent, fontWeight = FontWeight.Light, color = MaterialTheme.colorScheme.onBackground)
            }
            MyRow {
                Text(
                    if (lastMsg.length < maxMsgLength) lastMsg else lastMsg.substring(
                        0,
                        maxMsgLength
                    ) + "...",
                    color = if (read) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                if (msgNumberUnread > 0)
                    Text(
                        msgNumberUnread.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(4.dp)
                            .shadow(elevation = 1.dp, shape = CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(4.dp)
                            .padding(horizontal = 6.dp)
                    )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            error = UiError(false, "Account wasn't created"),
            onCloseDialog = {},
            onBottomBarClicked = {}
        )
    }
}
