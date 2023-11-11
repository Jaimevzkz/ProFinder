package com.vzkz.profinder.ui.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.LoginScreenDestination
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.ui.components.bottombar.MyBottomBar

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    when (profileViewModel.state.logout) {
        true -> {
            navigator.navigate(LoginScreenDestination) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true
                }
            }
        }

        false -> ScreenBody(profileViewModel) { navigator.navigate(it) }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    Scaffold(bottomBar = {
        MyBottomBar(
            currentDestination = ProfileScreenDestination,
            onClick = { onBottomBarClicked(it) })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column {
                Button(onClick = { profileViewModel.onLogout() }) {
                    Text(text = stringResource(R.string.logout))
                }
            }
        }
    }
}

@Preview
@Composable
fun LightPreview() {

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {

}