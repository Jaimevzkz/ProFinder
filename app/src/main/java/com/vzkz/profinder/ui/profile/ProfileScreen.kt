package com.vzkz.profinder.ui.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.PowerSettingsNew
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.Constants.ERRORSTR
import com.vzkz.profinder.core.USERMODELFORTESTS
import com.vzkz.profinder.destinations.EditProfileScreenDestination
import com.vzkz.profinder.destinations.LoginScreenDestination
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.RatingBar
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.dialogs.MyConfirmDialog
import com.vzkz.profinder.ui.components.starColor
import com.vzkz.profinder.ui.profile.shimmer.ProfileScreenShimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    if (profileViewModel.state.logout) {
        navigator.navigate(LoginScreenDestination) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }
    } else {
        profileViewModel.onInit()
        var user: ActorModel? by remember { mutableStateOf(null) }
        user = profileViewModel.state.user
        val loading = profileViewModel.state.loading
        val error = profileViewModel.state.error
        val darkTheme = profileViewModel.state.darkTheme

        if(user != null){
            ScreenBody(
                user = user!!,
                loading = loading,
                error = error,
                darkTheme = darkTheme,
                onThemeSwitch = profileViewModel::onThemeSwitch,
                onCloseDialog = { profileViewModel.onCloseDialog() },
                onChangeState = {
                    profileViewModel.onChangeState(user?.uid ?: ERRORSTR, it)
                },
                onLogout = { profileViewModel.onLogout() },
                onBottomBarClicked = { navigator.navigate(it) },
                onEditProfileClicked = { navigator.navigate(EditProfileScreenDestination) }
            )
        }
    }

}

@Composable
private fun ScreenBody(
    user: ActorModel,
    onLogout: () -> Unit,
    error: UiText?,
    darkTheme: Boolean,
    onThemeSwitch: () -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    loading: Boolean,
    onCloseDialog: () -> Unit,
    onChangeState: (ProfState) -> Unit,
    onEditProfileClicked: () -> Unit
) {
    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    val cardContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    val defaultVal = "- "
    var logout by remember { mutableStateOf(false) }

    MyBottomBarScaffold(
        currentDestination = ProfileScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) }
    ) { paddingValues ->
        if (loading) {
            ProfileScreenShimmer(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 16.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(bottom = 12.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var changeStateVisibility by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 20.dp)
                ) {
                    //Top screen
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { logout = true }) {
                            Icon(imageVector = Icons.Outlined.PowerSettingsNew, contentDescription = "logout")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { onEditProfileClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "App Profile Button"
                            )
                        }
                        MySpacer(size = 8)
                        IconButton(
                            onClick = onThemeSwitch
                        ) {
                            Icon(
                               imageVector = if(darkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                                contentDescription = "App Settings Button"
                            )
                        }

                    }

                    //Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfilePicture(modifier = Modifier, profilePhoto = user.profilePhoto, size = 100)
                        MySpacer(size = 4)
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = user.firstname,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user.lastname,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    val rating = user.rating ?: 0.0
                    MyColumn(Modifier.align(Alignment.CenterHorizontally)) {
                        RatingBar(
                            modifier = Modifier,
                            rating = rating,
                            starSize = 32,
                            starsColor = starColor(rating = rating),
                        )
                        Text(
                            text = if (user.rating != null) "${user.rating} (${user.reviewNumber} reviews)" else stringResource(
                                R.string.no_reviews_yet
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Light
                        )
                    }
                    MySpacer(size = 8)
                    if (user.actor == Actors.Professional) {
                        //State
                        MySpacer(size = 8)
                        Row(
                            modifier = Modifier
                                .clickable {
                                    changeStateVisibility = true
                                }
                                .shadow(elevation = 10.dp, shape = RoundedCornerShape(40))
                                .background(cardColor)
                                .padding(20.dp)
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {//Profession and state should never be null
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dot),
                                contentDescription = null,
                                tint = user.state?.tint ?: Color.Transparent,
                                modifier = Modifier
                                    .size(20.dp),
                            )
                            MySpacer(size = 16)
                            Text(
                                text = stringResource(id = user.profession!!.string),
                                style = MaterialTheme.typography.titleLarge,
                                color = cardContentColor
                            )
                        }
                    }
                    MySpacer(size = 16)
                    //profile_details
                    Column(
                        modifier = Modifier
                            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10))
                            .background(cardColor)
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
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
                            text = user.nickname, style = MaterialTheme.typography.titleLarge,
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
                            text = user.description ?: defaultVal, style = MaterialTheme.typography.titleLarge,
                            color = cardContentColor
                        )
                    }
                }

                if (changeStateVisibility) {
                    StateDialog(
                        modifier = Modifier
                            .align(Alignment.Center),
                        onStateClicked = {
                            if (user.state != it) {
                                onChangeState(it)
                            }
                            changeStateVisibility = false
                        }
                    )
                }
                if(error != null){
                    MyAlertDialog(
                        title = stringResource(R.string.error),
                        text = error.asString(),
                        onDismiss = { onCloseDialog() },
                        onConfirm = { onCloseDialog() },
                    )
                }

                MyConfirmDialog(
                    title = stringResource(id = R.string.logout),
                    text = stringResource(id = R.string.are_you_sure),
                    onDismiss = { logout = false },
                    onConfirm = { onLogout() },
                    showDialog = logout
                )
            }
        }
    }
}

@Composable
private fun StateDialog(
    modifier: Modifier = Modifier,
    onStateClicked: (ProfState) -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.tertiaryContainer
    MyColumn(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
            .background(containerColor)
            .padding(24.dp)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ProfState.entries.forEach {
            MySpacer(size = 16)
            MyRow(modifier = Modifier
                .clickable { onStateClicked(it) }
                .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.extraLarge)
                .background(containerColor)
                .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = null,
                    tint = it.tint,
                    modifier = Modifier
                        .size(20.dp),
                )
                MySpacer(size = 12)
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            MySpacer(size = 16)
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            user = USERMODELFORTESTS,
//            user = PROFESSIONALMODELFORTESTS,
            onLogout = { },
            onCloseDialog = {},
            error = null,
            darkTheme = true,
            onThemeSwitch = {},
            onBottomBarClicked = {},
//            loading = true,
            loading = false,
            onChangeState = {},
            onEditProfileClicked = {}
        )
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShimmerPreview() {
    ProFinderTheme {
        ScreenBody(
            user = USERMODELFORTESTS,
//            user = PROFESSIONALMODELFORTESTS,
            onLogout = { },
            onCloseDialog = {},
            error = null,
            darkTheme = true,
            onThemeSwitch = {},
            onBottomBarClicked = {},
            loading = true,
//            loading = false,
            onChangeState = {},
            onEditProfileClicked = {}
        )
    }
}