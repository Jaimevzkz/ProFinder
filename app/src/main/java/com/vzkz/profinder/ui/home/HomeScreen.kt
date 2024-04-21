package com.vzkz.profinder.ui.home

import android.Manifest
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R
import com.vzkz.profinder.core.JOBLISTFORTEST
import com.vzkz.profinder.core.PROFFESIONALLISTFORTEST
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.ViewProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.ClickableRatingBar
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.home.components.HomeCard
import com.vzkz.profinder.ui.home.components.HomeFavList
import com.vzkz.profinder.ui.home.components.HomeJobList
import com.vzkz.profinder.ui.home.components.HomeRequestList
import com.vzkz.profinder.ui.home.components.shimmer.FavListShimmer
import com.vzkz.profinder.ui.home.components.shimmer.HomeCardShimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, homeViewModel: HomeViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        homeViewModel.onInit()
    }
    RequestLocationPermission()
    val error = homeViewModel.state.error
    var favList: List<ActorModel> by remember { mutableStateOf(emptyList()) }
    favList = homeViewModel.state.favList
    var loading by remember { mutableStateOf(true) }
    loading = homeViewModel.state.loading
    var isUser by remember { mutableStateOf(false) }
    isUser = homeViewModel.state.isUser
    val requestList = homeViewModel.state.requestList
    val jobList = homeViewModel.state.jobList

    ScreenBody(
        favList = favList,
        requestList = requestList,
        jobList = jobList,
        error = error,
        loading = loading,
        isUser = isUser,
        onDeleteFav = { homeViewModel.onDeleteFav(it) },
        onCloseDialog = { homeViewModel.onCloseDialog() },
        onBottomBarClicked = { navigator.navigate(it) },
        onAcceptRequest = { homeViewModel.onAcceptRequest(it) },
        onRejectRequest = { rid, otherUid ->
            homeViewModel.onDeleteRequest(rid = rid, otherUid = otherUid)
        },
        onProfileInfo = {
            navigator.navigate(ViewProfileScreenDestination(uidToSee = it))
        },
        onRateUser = { jobModel, rating ->
            homeViewModel.onRateUser(job = jobModel, rating = rating)
        },
        onRateProf = { jobModel, rating ->
            homeViewModel.onRateProf(job = jobModel, rating = rating)
        },
    )
}

@Composable
private fun ScreenBody(
    favList: List<ActorModel>,
    requestList: List<JobModel>,
    jobList: List<JobModel>,
    isUser: Boolean,
    error: UiText?,
    loading: Boolean,
    onDeleteFav: (String) -> Unit,
    onAcceptRequest: (JobModel) -> Unit,
    onRejectRequest: (String, String) -> Unit,
    onCloseDialog: () -> Unit,
    onProfileInfo: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onRateUser: (JobModel, Int) -> Unit,
    onRateProf: (JobModel, Int) -> Unit
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
        var isRatingVisible by remember { mutableStateOf(false) }
        var jobToRate: JobModel? by remember { mutableStateOf(null) }
        var isUserRating by remember { mutableStateOf(false) }

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
                        contentPadding = contentPadding,
                        placeRight = false,
                        content = {
                            MySpacer(size = 4)
                            for (i in 0..2) {
                                Box(
                                    modifier = Modifier
                                        .shimmer()
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .height(80.dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
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
                        title = if (isUser) stringResource(R.string.pending_requests) else stringResource(
                            R.string.job_requests
                        ),
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
                            MySpacer(size = 4)
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
                        content = {
                            MySpacer(size = 4)
                            for (i in 0..2) {
                                Box(
                                    modifier = Modifier
                                        .shimmer()
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .height(80.dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                    )
                } else {
                    HomeCard( //Active jobs
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
                        HomeJobList(
                            jobList = jobList,
                            isUser = isUser,
                            onRateUser = {
                                isUserRating = false
                                jobToRate = it
                                isRatingVisible = true
                            },
                            onRateProf = {
                                isUserRating = true
                                jobToRate = it
                                isRatingVisible = true
                            },
                            onSeeProfile = onProfileInfo
                        )
                    }
                }
            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                RequestNotificationPermissionDialog()
//            }

            if (error != null) {
                MyAlertDialog(
                    title = stringResource(R.string.error),
                    text = error.asString(),
                    onDismiss = { onCloseDialog() },
                    onConfirm = { onCloseDialog() },
                )
            }


            if (isRatingVisible && jobToRate != null) {
                var rating by remember { mutableIntStateOf(0) }
                RatingDialog(
                    rating,
                    otherNickname = jobToRate?.otherNickname
                        ?: if (isUser) stringResource(R.string.the_professional) else stringResource(
                            R.string.the_client
                        ),
                    onRatingSet = { rating = it },
                    onHideDialog = { isRatingVisible = false },
                    onSubmitRating = {
                        isRatingVisible = false
                        if(isUserRating){
                            onRateProf(jobToRate!!, rating)
                        } else{
                            onRateUser(jobToRate!!, rating)
                        }
                    }
                )
            }
        }
    }

}

@Composable
private fun RatingDialog(
    rating: Int,
    otherNickname: String,
    onRatingSet: (Int) -> Unit,
    onHideDialog: () -> Unit,
    onSubmitRating: () -> Unit
) {
    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ), onDismissRequest = onHideDialog
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(24.dp)
        ) {
            MyColumn {
                Text(text = stringResource(R.string.rate_you_experience_with) + otherNickname)
                MySpacer(size = 4)
                ClickableRatingBar(
                    rating = rating,
                    onRatingSet = onRatingSet
                )
            }
            MyRow(
                modifier = Modifier
                    .padding(top = 92.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = onHideDialog
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
                MySpacer(size = 8)
                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = {
                        if (rating > 0)
                            onSubmitRating()
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }

            }

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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
    onPermissionsRevoked: () -> Unit = {}
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
//            favList = emptyList(),
            favList = PROFFESIONALLISTFORTEST,
            isUser = true,
            error = null,
            requestList = JOBLISTFORTEST,
            jobList = JOBLISTFORTEST,
//            loading = true,
            loading = false,
            onDeleteFav = {},
            onCloseDialog = {},
            onBottomBarClicked = {},
            onProfileInfo = {},
            onAcceptRequest = {},
            onRejectRequest = { _, _ -> },
            onRateUser = { _, _ -> },
            onRateProf = { _, _ -> }
        )
    }

}

