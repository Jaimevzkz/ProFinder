package com.vzkz.profinder.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.LoginScreenDestination
import com.vzkz.profinder.ui.components.MyImageLogo
import com.vzkz.profinder.ui.theme.ProFinderTheme

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    ScreenBody()
    when(splashViewModel.getDestination()){
        SplashDestinations.HomeDest -> navigator.navigate(HomeScreenDestination)
        SplashDestinations.LoginDest -> navigator.navigate(LoginScreenDestination)
    }
}


@Composable
private fun ScreenBody() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MyImageLogo()
        Text(text= "ProFinder",modifier = Modifier, style = MaterialTheme.typography.displayLarge)
    }
}

@Preview
@Composable
fun SplashPreview() {
    ProFinderTheme {
        ScreenBody()
    }
}