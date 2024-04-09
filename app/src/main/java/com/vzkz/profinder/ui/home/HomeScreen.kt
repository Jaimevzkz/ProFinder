package com.vzkz.profinder.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.PROFFESIONALLISTFORTEST
import com.vzkz.profinder.core.REQUESTLISTFORTEST
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.ViewProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.RequestModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.home.components.HomeCard
import com.vzkz.profinder.ui.home.components.HomeFavList
import com.vzkz.profinder.ui.home.components.shimmer.FavListShimmer
import com.vzkz.profinder.ui.home.components.shimmer.HomeCardShimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme
import com.vzkz.profinder.ui.theme.acceptColor
import com.vzkz.profinder.ui.theme.rejectColor

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, homeViewModel: HomeViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        homeViewModel.onInit()
    }
    val error = homeViewModel.state.error
    var favList: List<ActorModel> by remember { mutableStateOf(emptyList()) }
    favList = homeViewModel.state.favList
    var loading by remember { mutableStateOf(true) }
    loading = homeViewModel.state.loading
    val requestList = homeViewModel.state.requestList
    var isUser by remember { mutableStateOf(false) }
    isUser = homeViewModel.state.isUser
    ScreenBody(
        favList = favList,
        requestList = requestList,
        error = error,
        loading = loading,
        isUser = isUser,
        onDeleteFav = { homeViewModel.onDeleteFav(it) },
        onCloseDialog = { homeViewModel.onCloseDialog() },
        onBottomBarClicked = { navigator.navigate(it) },
        onAcceptRequest = {},
        onRejectRequest = { homeViewModel.onDeleteRequest(it) },
        onProfileInfo = {
            navigator.navigate(ViewProfileScreenDestination(uidToSee = it))
        }
    )
}

@Composable
private fun ScreenBody(
    favList: List<ActorModel>,
    requestList: List<RequestModel>,
    isUser: Boolean,
    error: UiError,
    loading: Boolean,
    onDeleteFav: (String) -> Unit,
    onAcceptRequest: (String) -> Unit,
    onRejectRequest: (String) -> Unit,
    onCloseDialog: () -> Unit,
    onProfileInfo: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    MyBottomBarScaffold(
        currentDestination = HomeScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) }
    ) { paddingValues ->
        val cardColor = MaterialTheme.colorScheme.surfaceVariant
        val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val fontFamily = FontFamily(Font(R.font.oswald))
        val cardPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        val contentPadding = PaddingValues(4.dp)
        var editFavList by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            MyColumn {
                if (loading) {
                    HomeCardShimmer(
                        modifier = Modifier
                            .weight(1f),
                        cardColor = cardColor,
                        cardPadding = cardPadding,
                        contentPadding = PaddingValues(12.dp),
                        placeRight = false,
                        content = { }
                    )
                } else {
                    HomeCard( //Hired Services
                        modifier = Modifier
                            .weight(1f),
                        boxModifier = Modifier.padding(top = 8.dp),
                        cardColor = cardColor,
                        contentColor = contentColor,
                        fontFamily = fontFamily,
                        cardPadding = cardPadding,
                        contentPadding = contentPadding,
                        title = if (isUser) "Pending requests" else "Job requests",
                        placeRight = false
                    ) {
                        HomeRequestList(
                            requestList = requestList,
                            isUser = isUser,
                            onSeeProfile = onProfileInfo,
                            onAcceptRequest = onAcceptRequest,
                            onRejectRequest = onRejectRequest
                        )
                    }
                }

                if (loading) {
                    HomeCardShimmer(
                        modifier = Modifier
                            .weight(1f),
                        cardColor = cardColor,
                        cardPadding = cardPadding,
                        contentPadding = contentPadding,
                        placeRight = true,
                        content = { FavListShimmer() }
                    )
                } else {
                    HomeCard( //Favorites
                        modifier = Modifier
                            .weight(1f),
                        boxModifier = Modifier,
                        cardColor = cardColor,
                        contentColor = contentColor,
                        fontFamily = fontFamily,
                        cardPadding = cardPadding,
                        contentPadding = contentPadding,
                        isEditFavListEmpty = favList.isEmpty(),
                        editFavList = editFavList,
                        onEditFavList = { editFavList = !editFavList },
                        title = "Favorites",
                        placeRight = true,
                        content = {
                            HomeFavList(
                                favList,
                                contentColor,
                                editFavList,
                                onDeleteFav = onDeleteFav,
                                onProfileInfo = onProfileInfo
                            )
                        }
                    )
                }

                if (loading) {
                    HomeCardShimmer(
                        modifier = Modifier
                            .weight(1f),
                        cardColor = cardColor,
                        cardPadding = cardPadding,
                        contentPadding = PaddingValues(12.dp),
                        placeRight = false,
                        content = { }
                    )
                } else {
                    HomeCard( //Recent
                        modifier = Modifier.weight(1f),
                        boxModifier = Modifier.padding(bottom = 8.dp),
                        cardColor = cardColor,
                        contentColor = contentColor,
                        fontFamily = fontFamily,
                        cardPadding = cardPadding,
                        contentPadding = contentPadding,
                        title = "Active jobs",
                        placeRight = false
                    ) {

                    }
                }
            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                RequestNotificationPermissionDialog()
//            }

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
fun HomeRequestList(
    requestList: List<RequestModel>,
    isUser: Boolean,
    onSeeProfile: (String) -> Unit,
    onAcceptRequest: (String) -> Unit,
    onRejectRequest: (String) -> Unit
) {
    val fontColor = MaterialTheme.colorScheme.onPrimaryContainer
    LazyColumn {
        items(requestList) { request ->
            MyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
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
                        onClick = { },
                        border = BorderStroke(1.dp, fontColor),
                        modifier = Modifier.padding(end = 2.dp)
                    ) {
                        Text(text = "Cancel", color = fontColor, fontSize = 12.sp)
                    }
                } else {
                    MyRow {
                        IconButton(
                            onClick = { onAcceptRequest(request.rid) },
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
                            onClick = { onRejectRequest(request.rid) },
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

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun RequestNotificationPermissionDialog() {
//    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
//
//    if (!permissionState.status.isGranted) {
//        if (permissionState.status.shouldShowRationale) RationaleDialog()
//        else PermissionDialog { permissionState.launchPermissionRequest() }
//    }
//}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
//            favList = emptyList(),
            favList = PROFFESIONALLISTFORTEST,
            isUser = false,
            error = UiError(false, "Account wasn't created"),
//            loading = true,
            requestList = REQUESTLISTFORTEST,
            loading = false,
            onDeleteFav = {},
            onCloseDialog = {},
            onBottomBarClicked = {},
            onProfileInfo = {},
            onAcceptRequest = {},
            onRejectRequest = {}
        )
    }

}

