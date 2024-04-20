package com.vzkz.profinder.ui.profile.viewprofile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R
import com.vzkz.profinder.core.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.core.SERVICELISTFORTEST
import com.vzkz.profinder.core.SERVICEMODEL1FORTEST
import com.vzkz.profinder.destinations.IndividualChatScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.RatingBar
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.shimmer.IconShimmer
import com.vzkz.profinder.ui.components.starColor
import com.vzkz.profinder.ui.services.components.userscreen.ServiceCard
import com.vzkz.profinder.ui.services.components.userscreen.ServiceDetailsDialog
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ViewProfileScreen(
    navigator: DestinationsNavigator,
    viewProfileViewModel: ViewProfileViewModel = hiltViewModel(),
    uidToSee: String
) {
    LaunchedEffect(key1 = true) {
        viewProfileViewModel.onInit(uidToSee)
    }
    val error = viewProfileViewModel.state.error
    var userToSee: ActorModel? by remember { mutableStateOf(null) }
    userToSee = viewProfileViewModel.state.userToSee
    val isFavourite = viewProfileViewModel.state.isFavourite
    val loading = viewProfileViewModel.state.loading
    val uid = viewProfileViewModel.state.uid

    ScreenBody(
        userToSee = userToSee ?: ActorModel(),
        loading = loading,
        error = error,
        isFavourite = isFavourite,
        serviceList = viewProfileViewModel.state.serviceList,
        requestExists = viewProfileViewModel.state.requestExists,
        onChangeFavourite = {
            viewProfileViewModel.onFavouriteChanged(uidToChange = uidToSee, add = !isFavourite)
        },
        onChatClicked = {
            if (uid != null) {
                navigator.navigate(
                    IndividualChatScreenDestination(
                        otherNickname = userToSee?.nickname ?: "Guess",
                        otherProfilePhoto = userToSee?.profilePhoto,
                        otherUid = uidToSee,
                        viewProfileViewModel.combineUids(uid, uidToSee)
                    )
                )
            }
        },
        onCheckRequestExists = {
            viewProfileViewModel.checkExistingRequests(it)
        },
        onRequestService = {
            viewProfileViewModel.onRequestService(it)
        },
        onCancelRequest = {
            viewProfileViewModel.onDeleteRequest(it)
        },
        onCloseDialog = {
            viewProfileViewModel.onCloseDialog()
        },
        onNavBack = {
            navigator.popBackStack()
        }
    )
}

@Composable
private fun ScreenBody(
    userToSee: ActorModel,
    error: UiText?,
    loading: Boolean,
    isFavourite: Boolean,
    serviceList: List<ServiceModel>,
    requestExists: ServiceState,
    onCancelRequest: (String) -> Unit,
    onRequestService: (ServiceModel) -> Unit,
    onChangeFavourite: () -> Unit,
    onChatClicked: () -> Unit,
    onCloseDialog: () -> Unit,
    onNavBack: () -> Unit,
    onCheckRequestExists: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val cardColor = MaterialTheme.colorScheme.surfaceVariant
        val cardContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val defaultVal = "- "
        var showServiceInfo by remember { mutableStateOf(false) }
        var serviceToShow: ServiceModel? by remember { mutableStateOf(null) }
        if (loading) {
            ViewProfileShimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp),
                cardColor = cardColor
            )
        } else {
            val modifier = if(!showServiceInfo) Modifier.verticalScroll(rememberScrollState()) else Modifier
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp)
            ) {
                //Header
                MySpacer(size = 32)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfilePicture(
                        modifier = Modifier,
                        profilePhoto = userToSee.profilePhoto,
                        size = 100
                    )
                    MySpacer(size = 4)
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = userToSee.firstname,
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userToSee.lastname,
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                //Status
                MySpacer(size = 8)
                val rating = userToSee.rating ?: 0.0
                MyColumn(Modifier.align(Alignment.CenterHorizontally)) {
                    RatingBar(
                        modifier = Modifier,
                        rating = rating,
                        starSize = 32,
                        starsColor = starColor(rating = rating),
                    )
                    Text(
                        text = if (userToSee.rating != null) "${userToSee.rating} (${userToSee.reviewNumber} reviews)" else stringResource(
                            R.string.no_reviews_yet
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Light
                    )
                }
                MySpacer(size = 8)
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    IconButton(onClick = {
                        onChangeFavourite()
                    }, modifier = Modifier) {
                        Icon(
                            imageVector = if (!isFavourite) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                            contentDescription = "Favourite",
                            tint = if (!isFavourite) MaterialTheme.colorScheme.onBackground else Color.Red
                        )
                    }
                    if (userToSee.actor == Actors.Professional) {
                        MySpacer(size = 8)
                        Row(
                            modifier = Modifier
                                .shadow(elevation = 1.dp, shape = RoundedCornerShape(40))
                                .background(cardColor)
                                .padding(12.dp)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {//Profession and state should never be null
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dot),
                                contentDescription = null,
                                tint = userToSee.state?.tint ?: Color.Transparent,
                                modifier = Modifier
                                    .size(20.dp),
                            )
                            MySpacer(size = 16)
                            Text(
                                text = userToSee.profession!!.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = cardContentColor
                            )
                        }
                    }
                    MySpacer(size = 8)

                    IconButton(
                        onClick = onChatClicked,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Chat,
                            contentDescription = "Chat",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                MySpacer(size = 16)

                //profile_details
                var expanded by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(10))
                        .background(cardColor)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    val spaceBetween = 14
                    val innerSpaceBetween = 6
                    MySpacer(size = spaceBetween)
                    Text(
                        text = stringResource(R.string.nickname),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = cardContentColor
                    )
                    MySpacer(size = innerSpaceBetween)
                    Text(
                        text = userToSee.nickname, style = MaterialTheme.typography.titleLarge,
                        color = cardContentColor
                    )
                    MySpacer(size = spaceBetween)
                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = cardContentColor
                    )
                    MySpacer(size = innerSpaceBetween)
                    Text(
                        modifier = Modifier.clickable { expanded = !expanded },
                        text = userToSee.description ?: defaultVal,
                        style = MaterialTheme.typography.titleLarge,
                        color = cardContentColor,
                        maxLines = if (!expanded) 4 else Int.MAX_VALUE
                    )
                    if (userToSee.description != null && !expanded) {
                        Icon(
                            imageVector = Icons.Filled.ExpandMore,
                            contentDescription = "expand description",
                            tint = cardContentColor,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    } else if(userToSee.description != null) {
                        Icon(
                            imageVector = Icons.Filled.ExpandLess,
                            contentDescription = "expand less description",
                            tint = cardContentColor,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.SemiBold,
                    text = "Services",
                    style = MaterialTheme.typography.titleLarge,
                    color = cardContentColor
                )
                serviceList.forEach {serviceModel ->
                    MySpacer(size = 8)
                    ServiceCard(
                        modifier = Modifier,
                        userCalling = true,
                        buttonsEnabled = !showServiceInfo,
                        service = serviceModel,
                        backgroundColor = cardColor,
                        fontColor = cardContentColor,
                        onServiceInfo ={
                            onCheckRequestExists(serviceModel.sid)
                            serviceToShow = serviceModel
                            showServiceInfo = true
                        }
                    )
                }
            }
        }
        if (serviceToShow != null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
            ) {
                ServiceDetailsDialog(
                    modifier = Modifier
                        .align(Alignment.Center),
                    isVisible = showServiceInfo,
                    service = serviceToShow!!,
                    requestExists = requestExists,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    fontColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onSeeProfile = {},
                    onRequest = { onRequestService(serviceToShow!!) },
                    onCancelRequest = onCancelRequest,
                    onCloseDialog = {
                        serviceToShow = null
                        showServiceInfo = false
                    }
                )
            }
        }
        IconButton(
            enabled = !showServiceInfo,
            onClick = { onNavBack() }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Navigate back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        if (error != null) {
            MyAlertDialog(
                title = stringResource(R.string.error),
                text = error.asString(),
                onDismiss = { onCloseDialog() },
                onConfirm = { onCloseDialog() },
            )
        }
    }
}

@Composable
private fun ViewProfileShimmer(modifier: Modifier = Modifier, cardColor: Color) {
    Column(
        modifier = modifier
    ) {
        MySpacer(size = 32)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .shimmer()
                    .size(100.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = 4)
            Column(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(130.dp)
                        .height(40.dp)
                        .background(Color.Gray)
                )
                MySpacer(size = 8)
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(180.dp)
                        .height(40.dp)
                        .background(Color.Gray)
                )

            }
        }
        //Status
        MySpacer(size = 8)
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconShimmer()
            Spacer(modifier = Modifier.weight(2.5f))
            IconShimmer()
            Spacer(modifier = Modifier.weight(1f))
        }
        MySpacer(size = 16)

        //profile_details
        Column(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(10))
                .background(cardColor)
                .padding(horizontal = 16.dp)
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val spaceBetween = 14
            val innerSpaceBetween = 6
            MySpacer(size = spaceBetween)
            Box(
                modifier = Modifier
                    .shimmer()
                    .width(90.dp)
                    .height(20.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = innerSpaceBetween)
            Box(
                modifier = Modifier
                    .shimmer()
                    .width(100.dp)
                    .height(20.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = spaceBetween)
            Box(
                modifier = Modifier
                    .shimmer()
                    .width(90.dp)
                    .height(20.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = innerSpaceBetween)
            for (i in 0..5) {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.Gray)
                )
                MySpacer(size = 6)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            userToSee = PROFESSIONALMODELFORTESTS,
            error = null,
            isFavourite = true,
            requestExists = ServiceState.REQUESTED,
            onChangeFavourite = {},
            onChatClicked = {},
            onCloseDialog = {},
            onNavBack = {},
            onRequestService = {},
            onCancelRequest = {},
            onCheckRequestExists = {},
            loading = false,
            serviceList = SERVICELISTFORTEST
        )
    }

}
