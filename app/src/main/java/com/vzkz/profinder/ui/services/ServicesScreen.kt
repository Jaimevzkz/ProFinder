package com.vzkz.profinder.ui.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.SERVICELISTFORTEST
import com.vzkz.profinder.destinations.ServicesScreenDestination
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ServiceCard
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.AddServiceDialog
import com.vzkz.profinder.ui.components.dialogs.MyConfirmDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ServicesScreen(
    navigator: DestinationsNavigator,
    servicesViewModel: ServicesViewModel = hiltViewModel()
) {
    servicesViewModel.onInit()
    val loading = servicesViewModel.state.loading
    var activeServices: List<ServiceModel> by remember { mutableStateOf(emptyList()) }
    activeServices = servicesViewModel.state.activeServiceList
    var inActiveServices: List<ServiceModel> by remember { mutableStateOf(emptyList()) }
    inActiveServices = servicesViewModel.state.inActiveServiceList

    ScreenBody(
        loading = loading,
        activeServices = activeServices,
        inactiveServices = inActiveServices,
        onActivityChange = { service ->
            servicesViewModel.onChangeActivity(service)
        },
        onServiceAdded = { servicesViewModel.onAddService(it) },
        onServiceDeleted = { sid ->
            servicesViewModel.onDeleteService(sid)
        },
        onBottomBarClicked = { navigator.navigate(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    loading: Boolean,
    activeServices: List<ServiceModel>,
    inactiveServices: List<ServiceModel>,
    onActivityChange: (ServiceModel) -> Unit,
    onServiceAdded: (ServiceModel) -> Unit,
    onServiceDeleted: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    var addDialogVisibility by remember { mutableStateOf(false) }
    var confirmDialogVisibility by remember { mutableStateOf(false) }
    var sidToDelete by remember { mutableStateOf("") }


    MyBottomBarScaffold(
        currentDestination = ServicesScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Services",
                        style = MaterialTheme.typography.displaySmall,
                    )
                },
                colors = centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addDialogVisibility = true },
                modifier = Modifier,
                shape = CircleShape

            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        },
    ) { paddingValues ->
        if (loading) {
            MyCircularProgressbar()
        } else {
            Box(contentAlignment = Alignment.TopCenter) {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "Active",
                            modifier = Modifier.align(Alignment.TopStart),
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    items(activeServices) { serviceModel ->
                        MySpacer(size = 12)
                        ServiceCard(
                            title = serviceModel.name,
                            category = serviceModel.category,
                            description = serviceModel.servDescription,
                            onDelete = {
                                sidToDelete = serviceModel.sid
                                confirmDialogVisibility = true
                            },
                            active = serviceModel.isActive,
                            onActivityChange = { onActivityChange(serviceModel) }
                        )
                    }
                    item { MySpacer(size = 12) }
                    item {
                        Text(
                            text = "Inactive",
                            modifier = Modifier.align(Alignment.TopStart),
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    items(inactiveServices) { serviceModel ->
                        MySpacer(size = 12)
                        ServiceCard(
                            title = serviceModel.name,
                            category = serviceModel.category,
                            description = serviceModel.servDescription,
                            onDelete = {
                                sidToDelete = serviceModel.sid
                                confirmDialogVisibility = true
                            },
                            active = serviceModel.isActive,
                            onActivityChange = { onActivityChange(serviceModel) }
                        )
                    }
                }
                if (addDialogVisibility) {
                    AddServiceDialog(
                        onDismiss = { addDialogVisibility = false },
                        onConfirm = {
                            addDialogVisibility = false
                            onServiceAdded(it)
                        }
                    )
                }
                MyConfirmDialog(
                    title = stringResource(R.string.are_you_sure),
                    text = stringResource(R.string.deletions_are_not_undoable),
                    onDismiss = { confirmDialogVisibility = false },
                    onConfirm = {
                        onServiceDeleted(sidToDelete)
                        confirmDialogVisibility = false
                    },
                    showDialog = confirmDialogVisibility
                )
            }
        }
    }
}

@Preview
@Composable
fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            loading = false,
            activeServices = SERVICELISTFORTEST,
            inactiveServices = SERVICELISTFORTEST,
            onActivityChange = {},
            onServiceAdded = {},
            onServiceDeleted = {},
            onBottomBarClicked = {}
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}