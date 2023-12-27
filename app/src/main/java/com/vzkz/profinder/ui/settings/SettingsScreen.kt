package com.vzkz.profinder.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable

fun SettingsScreen(
    navigator: DestinationsNavigator,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    settingsViewModel.onInit()
    val darkTheme = settingsViewModel.state.darkTheme
    ScreenBody(
        darkTheme = darkTheme,
        onThemeSwitch = { settingsViewModel.onThemeSwitch() },
        onBackClicked = { navigator.navigate(ProfileScreenDestination) }
    )
}

@Composable
private fun ScreenBody(
    darkTheme: Boolean,
    onThemeSwitch: () -> Unit,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = {
            onBackClicked()
        }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Cancel")
        }
        Column(
            Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.dark_mode))
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = darkTheme, onCheckedChange = { onThemeSwitch() })

            }
        }
    }
}


@Preview
@Composable
fun LightPreview() {
    ProFinderTheme {
        ScreenBody(darkTheme = false, onThemeSwitch = {}) {

        }
    }
}