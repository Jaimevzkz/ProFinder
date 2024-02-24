package com.vzkz.profinder.ui.services

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.maps.android.compose.GoogleMap
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.core.boilerplate.SERVICELISTFORTEST
import com.vzkz.profinder.destinations.ServicesScreenDestination
import com.vzkz.profinder.destinations.ViewProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ServiceCard
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.AddServiceDialog
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.dialogs.MyConfirmDialog
import com.vzkz.profinder.ui.components.dialogs.ServiceDetailsDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ServicesScreen(
    navigator: DestinationsNavigator,
    servicesViewModel: ServicesViewModel = hiltViewModel()
) {
    //todo
    // - develop lista de favoritos
    // - develop fotos de perfil
    servicesViewModel.onInit()

    val loading = servicesViewModel.state.loading
    var activeServices: List<ServiceModel> by remember { mutableStateOf(emptyList()) }
    activeServices = servicesViewModel.state.activeServiceList
    var inActiveServices: List<ServiceModel> by remember { mutableStateOf(emptyList()) }
    inActiveServices = servicesViewModel.state.inActiveServiceList
    val error = servicesViewModel.state.error
    var user: ActorModel? by remember { mutableStateOf(null) }
    user = servicesViewModel.state.user
    var actor: Actors? by remember { mutableStateOf(null) }
    actor = user?.actor

    ScreenBody(
        user = user ?: ActorModel(),
        actor = actor,
        loading = loading,
        activeServices = activeServices,
        inactiveServices = inActiveServices,
        error = error,
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
    inactiveServices: List<ServiceModel>,
    error: UiError,
    onCloseDialog: () -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
    onServiceAdded: (ServiceModel) -> Unit,
    onServiceDeleted: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onSeeProfile: (ActorModel) -> Unit
) {
    var addDialogVisibility by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(true) }
    MyBottomBarScaffold(
        currentDestination = ServicesScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) },
        topBar = {
            if(!showMap){
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Services",
                            style = MaterialTheme.typography.displaySmall,
                        )
                    },
                    colors = centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
        when (actor) {
            Actors.User -> {
                if(showMap){
                    MapScreenBody(
                        modifier = Modifier.padding(paddingValues),
                        onSeeList = { showMap = false }
                    )
                } else{
                    UserScreenBody(
                        modifier = Modifier.padding(paddingValues),
                        serviceList = activeServices,
                        onSeeProfile = { onSeeProfile(it) },
                        onSeeMap = { showMap = true }
                    )
                }
            }

            Actors.Professional -> {
                ProfessionalScreenBody(
                    modifier = Modifier.padding(paddingValues),
                    user = user,
                    loading = loading,
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

            else -> MyCircularProgressbar()
        }
    }

}

@Composable
private fun MapScreenBody(
    modifier: Modifier = Modifier,
    onSeeList: () -> Unit
){
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMap {

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .padding(bottom = 20.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            onClick = { onSeeList() }
        ) {
            Icon(
                imageVector = Icons.Outlined.FormatListNumbered,
                contentDescription = "Show list view",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun UserScreenBody(
    modifier: Modifier = Modifier,
    serviceList: List<ServiceModel>,
    onSeeProfile: (ActorModel) -> Unit,
    onSeeMap: () -> Unit
) {

    var query by remember { mutableStateOf("") }
    var showServiceInfo by remember { mutableStateOf(false) }
    var serviceToShow: ServiceModel? by remember { mutableStateOf(null) }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        MyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            MyGenericTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                hint = stringResource(R.string.type_a_service_to_search),
                shape = CircleShape,
                text = query,
                onTextChanged = {
                    query = it
                }
            )
            MySpacer(size = 16)
            LazyColumn {
                items(serviceList.filter {
                    it.name.contains(
                        other = query,
                        ignoreCase = true
                    )
                }) { serviceModel ->
                    ServiceCard(
                        userCalling = true,
                        service = serviceModel,
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        fontColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onServiceInfo = {
                            serviceToShow = serviceModel
                            showServiceInfo = true
                        },
                        onDelete = {/*do nothing*/},
                        onActiveChange = {/*do nothing*/}
                    )
                    MySpacer(size = 16)
                }
            }

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            onClick = { onSeeMap() }
        ) {
            Icon(
                imageVector = Icons.Outlined.Map,
                contentDescription = "Show map view",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    if (serviceToShow != null) {
        Box(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
        ) {
            ServiceDetailsDialog(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 12.dp),
                isVisible = showServiceInfo,
                service = serviceToShow!!,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                fontColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onSeeProfile = { if(serviceToShow != null) onSeeProfile(serviceToShow!!.owner) },
                onRequest = { /*TODO*/ },
                onCloseDialog = {
                    serviceToShow = null
                    showServiceInfo = false
                }
            )
        }
    }

}

@Composable
private fun ProfessionalScreenBody(
    modifier: Modifier = Modifier,
    user: ActorModel,
    loading: Boolean,
    activeServices: List<ServiceModel>,
    inactiveServices: List<ServiceModel>,
    error: UiError,
    addDialogVisibility: Boolean,
    onConfirmDialogVisibilityChange: (Boolean) -> Unit,
    onCloseDialog: () -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
    onServiceAdded: (ServiceModel) -> Unit,
    onServiceDeleted: (String) -> Unit,
) {
    var confirmDialogVisibility by remember { mutableStateOf(false) }
    var sidToDelete by remember { mutableStateOf("") }
    var showActiveServices by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyColumn(Modifier.padding(12.dp)) {
                MyRow {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.active),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable {
                            showActiveServices = true
                        }
                    )
                    Spacer(modifier = Modifier.weight(2f))
                    Text(
                        text = stringResource(R.string.inactive),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable {
                            showActiveServices = false
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                MySpacer(size = 4)
                MyRow {
                    if (!showActiveServices) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Divider(
                        modifier = Modifier.weight(1f),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (showActiveServices) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            val serviceList = if (showActiveServices) activeServices else inactiveServices
            ShowProfessionalServiceList(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp),
                loading = loading,
                servicesToShow = serviceList,
                onChangeSidToDelete = { sidToDelete = it },
                onConfirmDialogVisibilityChange = { confirmDialogVisibility = it },
                onActivityChange = onActivityChange,
            )
        }

        if (addDialogVisibility) {
            AddServiceDialog(
                onDismiss = { onConfirmDialogVisibilityChange(false) },
                onConfirm = {
                    onConfirmDialogVisibilityChange(false)
                    onServiceAdded(it)
                },
                owner = user
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
        MyAlertDialog(
            title = stringResource(R.string.error),
            text = error.errorMsg.orEmpty(),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = error.isError
        )
    }
}

@Composable
private fun ShowProfessionalServiceList(
    modifier: Modifier = Modifier,
    loading: Boolean,
    servicesToShow: List<ServiceModel>,
    onChangeSidToDelete: (String) -> Unit,
    onConfirmDialogVisibilityChange: (Boolean) -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
) {
    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    val cardContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    if (loading) {
        MyCircularProgressbar()
    } else {
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(servicesToShow) { serviceModel ->
                ServiceCard(
                    userCalling = false,
                    service = serviceModel,
                    backgroundColor = cardColor,
                    fontColor = cardContentColor,
                    onServiceInfo = {/*Do nothing*/},
                    onDelete = {
                        onChangeSidToDelete(serviceModel.sid)
                        onConfirmDialogVisibilityChange(true)
                    },
                    onActiveChange = { onActivityChange(serviceModel) }
                )
                MySpacer(size = 16)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    ProFinderTheme {
        ScreenBody(
            actor = Actors.User,
            loading = false,
            activeServices = SERVICELISTFORTEST,
            inactiveServices = SERVICELISTFORTEST,
            error = UiError(false, ""),
            onActivityChange = {},
            onServiceAdded = {},
            onServiceDeleted = {},
            onBottomBarClicked = {},
            onCloseDialog = {},
            user = PROFESSIONALMODELFORTESTS,
            onSeeProfile = {}
        )
    }
}