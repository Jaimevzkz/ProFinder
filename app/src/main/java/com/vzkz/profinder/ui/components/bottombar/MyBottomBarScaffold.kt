package com.vzkz.profinder.ui.components.bottombar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBarScaffold(
    modifier: Modifier = Modifier,
    currentDestination: DirectionDestinationSpec,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            MyBottomBar(
                currentDestination = currentDestination,
                onClick = { if (currentDestination != it) onBottomBarClicked(it) }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

