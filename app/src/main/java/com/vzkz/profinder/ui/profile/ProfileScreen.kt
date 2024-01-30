package com.vzkz.profinder.ui.profile

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.vzkz.profinder.core.boilerplate.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.destinations.EditProfileScreenDestination
import com.vzkz.profinder.destinations.LoginScreenDestination
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.destinations.SettingsScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Constants.ERRORSTR
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.theme.ProFinderTheme
import com.vzkz.profinder.ui.theme.dialogContainer

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

        ScreenBody(
            user = user,
            loading = loading,
            onChangeState = {
                profileViewModel.onChangeState(user?.uid ?: ERRORSTR, it)
            },
            onLogout = { profileViewModel.onLogout() },
            onBottomBarClicked = { navigator.navigate(it) },
            onSettingsClicked = { navigator.navigate(SettingsScreenDestination) },
            onEditProfileClicked = { navigator.navigate(EditProfileScreenDestination) }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    user: ActorModel?,
    onLogout: () -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    loading: Boolean,
    onChangeState: (ProfState) -> Unit,
    onSettingsClicked: () -> Unit,
    onEditProfileClicked: () -> Unit
) {
    val defaultVal = "- "
    var nickname by remember { mutableStateOf("") }
    nickname = user?.nickname ?: defaultVal
    var firstname by remember { mutableStateOf("") }
    firstname = user?.firstname ?: defaultVal
    var lastname by remember { mutableStateOf("") }
    lastname = user?.lastname ?: defaultVal
    var description by remember { mutableStateOf("") }
    description = user?.description ?: defaultVal
    var actor: Actors by remember { mutableStateOf(Actors.User) }
    actor = user?.actor ?: Actors.User
    var profession: Professions? by remember { mutableStateOf(null) }
    profession = user?.profession
    var state: ProfState? by remember { mutableStateOf(null) }
    state = user?.state

    MyBottomBarScaffold(
        currentDestination = ProfileScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) }
    ) { paddingValues ->
        if (loading) {
            MyCircularProgressbar()
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(), contentAlignment = Alignment.Center
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
                            .align(Alignment.End)
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            onClick = { onSettingsClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
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
                        Image(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(shape = CircleShape)
                                .padding(12.dp),
                            painter = painterResource(id = R.drawable.defaultprofile),
                            contentDescription = "Profile photo",
                            contentScale = ContentScale.Crop
                        )
                        MySpacer(size = 4)
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = firstname,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = lastname,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (actor == Actors.Professional) {
                        //State
                        MySpacer(size = 8)
                        Row(
                            modifier = Modifier
                                .clickable {
                                    changeStateVisibility = true
                                }
                                .shadow(elevation = 10.dp, shape = RoundedCornerShape(40))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(20.dp)
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {//Profession and state should never be null
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dot),
                                contentDescription = null,
                                tint = state?.tint ?: Color.Transparent,
                                modifier = Modifier
                                    .size(20.dp),
                            )
                            MySpacer(size = 16)
                            Text(
                                text = profession!!.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    MySpacer(size = 16)
                    //profile_details
                    Column(
                        modifier = Modifier
                            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
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
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        MySpacer(size = innerSpaceBetween)
                        Text(
                            text = nickname, style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        MySpacer(size = spaceBetween)
                        Text(
                            text = stringResource(R.string.description),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        MySpacer(size = innerSpaceBetween)
                        Text(
                            text = description, style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    //Footer
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                            .padding(16.dp),
                        onClick = { onLogout() }
                    ) {
                        Text(text = stringResource(R.string.logout))
                    }
                }

                if (changeStateVisibility) {
                    StateDialog(
                        modifier = Modifier
                            .align(Alignment.Center),
                        onStateClicked = {
                            if (state != it) {
                                onChangeState(it)
                            }
                            changeStateVisibility = false
                        }
                    )
                }
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
                Text(text = it.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
            MySpacer(size = 16)
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
//            user = USERMODELFORTESTS,
            user = PROFESSIONALMODELFORTESTS,
            onLogout = { },
            onBottomBarClicked = {},
            loading = false,
            onChangeState = {},
            onSettingsClicked = { }
        ) {

        }
    }
}