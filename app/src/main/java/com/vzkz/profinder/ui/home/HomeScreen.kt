package com.vzkz.profinder.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.ChatScreenDestination
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, tViewModel: tViewModel = hiltViewModel()) {
    val error = tViewModel.state.error
    ScreenBody(
        error = error,
        onCloseDialog = {
            tViewModel.onCloseDialog()
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
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
