package com.vzkz.profinder.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    ScreenBody { navigator.navigate(it) }
}

@Composable
private fun ScreenBody(onBottomBarClicked: (DirectionDestinationSpec) -> Unit) {
    MyBottomBarScaffold(
        currentDestination = HomeScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = "Functionality not yet developed...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun LightPreview() {
    ProFinderTheme {
        ScreenBody {}
    }
}
