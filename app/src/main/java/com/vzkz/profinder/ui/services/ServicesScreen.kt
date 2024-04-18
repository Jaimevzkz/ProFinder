package com.vzkz.profinder.ui.services

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.core.SERVICELISTFORTEST
import com.vzkz.profinder.destinations.ServicesScreenDestination
import com.vzkz.profinder.destinations.ViewProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.services.components.ServicesScreenShimmer
import com.vzkz.profinder.ui.services.components.professionalscreen.ProfessionalScreenBody
import com.vzkz.profinder.ui.services.components.userscreen.MapScreenBody
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState
import com.vzkz.profinder.ui.services.components.userscreen.UserScreenBody
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ServicesScreen(
    navigator: DestinationsNavigator,
    servicesViewModel: ServicesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        servicesViewModel.onInit()
    }

    val loading = servicesViewModel.state.loading
    var activeServices: List<ServiceModel> by remember { mutableStateOf(emptyList()) }
    activeServices = servicesViewModel.state.activeServiceList
    var inActiveServices: List<ServiceModel> by remember { mutableStateOf(emptyList()) }
    inActiveServices = servicesViewModel.state.inActiveServiceList
    var user: ActorModel? by remember { mutableStateOf(null) }
    user = servicesViewModel.state.user
    var actor: Actors? by remember { mutableStateOf(null) }
    actor = user?.actor
    var requestExists by remember { mutableStateOf(ServiceState.FREE) }
    requestExists = servicesViewModel.state.requestExists

    ScreenBody(
        user = user ?: ActorModel(),
        actor = actor,
        loading = loading,
        activeServices = activeServices,
        requestExists = requestExists,
        inactiveServices = inActiveServices,
        error = servicesViewModel.state.error,
        onCheckRequestExists = { servicesViewModel.checkExistingRequests(it) },
        onRequestService = { servicesViewModel.onRequestService(it) },
        onCancelRequest = { servicesViewModel.onDeleteRequest(it) },
        onCloseDialog = { servicesViewModel.onCloseDialog() },
        onActivityChange = { service ->
            servicesViewModel.onChangeActivity(service)
        },
        onServiceAdded = { servicesViewModel.onAddService(it) },
        onServiceDeleted = { sid ->
            servicesViewModel.onDeleteService(sid)
        },
        onBottomBarClicked = { navigator.navigate(it) },
        onSeeProfile = {
            servicesViewModel.onSetProfileToSee(it)
            navigator.navigate(ViewProfileScreenDestination(it.uid))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    user: ActorModel,
    actor: Actors?,
    loading: Boolean,
    activeServices: List<ServiceModel>,
    requestExists: ServiceState,
    inactiveServices: List<ServiceModel>,
    error: UiText?,
    onCheckRequestExists: (String) -> Unit,
    onRequestService: (ServiceModel) -> Unit,
    onCancelRequest: (String) -> Unit,
    onCloseDialog: () -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
    onServiceAdded: (ServiceModel) -> Unit,
    onServiceDeleted: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onSeeProfile: (ActorModel) -> Unit
) {
    var addDialogVisibility by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }
    MyBottomBarScaffold(
        currentDestination = ServicesScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) },
        topBar = {
            if (!showMap) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.services),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            }
        },
        floatingActionButton = {
            if (actor == Actors.Professional) {
                FloatingActionButton(
                    onClick = { addDialogVisibility = true },
                    modifier = Modifier,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        },
    ) { paddingValues ->
        if (loading) {
            ServicesScreenShimmer(
                modifier = Modifier.padding(paddingValues),
                cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
            )
        } else {
            when (actor) {
                Actors.User -> {
                    if (showMap) {
                        MapScreenBody(
                            modifier = Modifier.padding(paddingValues),
                            onSeeList = { showMap = false }
                        )
                    } else {
                        UserScreenBody(
                            modifier = Modifier.padding(paddingValues),
                            serviceList = activeServices,
                            requestExists = requestExists,
                            onSeeProfile = { onSeeProfile(it) },
                            onSeeMap = { showMap = true },
                            onCheckRequestExists = onCheckRequestExists,
                            onRequestService = { onRequestService(it) },
                            onCancelRequest = onCancelRequest,
                            onCloseDialog = { onCloseDialog() },
                            error = error
                        )
                    }
                }

                Actors.Professional -> {
                    ProfessionalScreenBody(
                        modifier = Modifier.padding(paddingValues),
                        user = user,
                        activeServices = activeServices,
                        inactiveServices = inactiveServices,
                        error = error,
                        addDialogVisibility = addDialogVisibility,
                        onConfirmDialogVisibilityChange = { addDialogVisibility = it },
                        onCloseDialog = { onCloseDialog() },
                        onActivityChange = { service ->
                            onActivityChange(service)
                        },
                        onServiceAdded = { onServiceAdded(it) },
                        onServiceDeleted = { sid ->
                            onServiceDeleted(sid)
                        }
                    )
                }

                else -> ServicesScreenShimmer(
                    modifier = Modifier.padding(paddingValues),
                    cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    ProFinderTheme {
        ScreenBody(
//            actor = null,
//            actor = Actors.Professional,
            actor = Actors.User,
//            loading = true,
            loading = false,
            activeServices = SERVICELISTFORTEST,
            inactiveServices = SERVICELISTFORTEST,
            requestExists = ServiceState.FREE,
            error = null,
            onCheckRequestExists = {},
            onActivityChange = {},
            onServiceAdded = {},
            onServiceDeleted = {},
            onBottomBarClicked = {},
            onCloseDialog = {},
            user = PROFESSIONALMODELFORTESTS,
            onSeeProfile = {},
            onRequestService = {},
            onCancelRequest = {}
        )
    }
}