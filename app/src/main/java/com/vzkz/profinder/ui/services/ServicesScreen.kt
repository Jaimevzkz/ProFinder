package com.vzkz.profinder.ui.services

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.core.boilerplate.SERVICELISTFORTEST
import com.vzkz.profinder.destinations.ServicesScreenDestination
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
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ServicesScreen(
    navigator: DestinationsNavigator,
    servicesViewModel: ServicesViewModel = hiltViewModel()
) {
    //todo
    // - arreglar que no se muestre de primeras la pantalla de user
    // - configurar bien la lista de servicios de user 
    // - develop busqueda
    // - develop view profile
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
        actor = actor ?: Actors.User,
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
        onBottomBarClicked = { navigator.navigate(it) }
    )
}

@Composable
private fun ScreenBody(
    user: ActorModel,
    actor: Actors,
    loading: Boolean,
    activeServices: List<ServiceModel>,
    inactiveServices: List<ServiceModel>,
    error: UiError,
    onCloseDialog: () -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
    onServiceAdded: (ServiceModel) -> Unit,
    onServiceDeleted: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    if (actor == Actors.User) {
        UserScreenBody(
            serviceList = activeServices,
            onBottomBarClicked = { onBottomBarClicked(it) }
        )
    } else {
        ProfessionalScreenBody(
            user = user,
            loading = loading,
            activeServices = activeServices,
            inactiveServices = inactiveServices,
            error = error,
            onCloseDialog = { onCloseDialog() },
            onActivityChange = { service ->
                onActivityChange(service)
            },
            onServiceAdded = { onServiceAdded(it) },
            onServiceDeleted = { sid ->
                onServiceDeleted(sid)
            },
            onBottomBarClicked = { onBottomBarClicked(it) }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenBody(
    serviceList: List<ServiceModel>,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
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
                colors = centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
    ) { paddingValues ->
        var query by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    items(serviceList) { serviceModel ->
                        ServiceCard(
                            userCalling = true,
                            service = serviceModel,
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                            fontColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            onServiceInfo = {
                                //todo
                            },
                            onDelete = {
                                //do nothing
                            },
                            onActiveChange = {
                                //do nothing
                            }
                        )
                        MySpacer(size = 16)
                    }
                }

            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    ,
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Outlined.Map, contentDescription = "Show map view")
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfessionalScreenBody(
    user: ActorModel,
    loading: Boolean,
    activeServices: List<ServiceModel>,
    inactiveServices: List<ServiceModel>,
    error: UiError,
    onCloseDialog: () -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
    onServiceAdded: (ServiceModel) -> Unit,
    onServiceDeleted: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    var addDialogVisibility by remember { mutableStateOf(false) }
    var confirmDialogVisibility by remember { mutableStateOf(false) }
    var sidToDelete by remember { mutableStateOf("") }
    var showActiveServices by remember { mutableStateOf(true) }

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
                colors = centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addDialogVisibility = true },
                modifier = Modifier,
                shape = MaterialTheme.shapes.medium,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    user = user,
                    loading = loading,
                    servicesToShow = serviceList,
                    onChangeSidToDelete = { sidToDelete = it },
                    onConfirmDialogVisibilityChange = { confirmDialogVisibility = it },
                    onActivityChange = onActivityChange,
                )
            }

            if (addDialogVisibility) {
                AddServiceDialog(
                    onDismiss = { addDialogVisibility = false },
                    onConfirm = {
                        addDialogVisibility = false
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
}

@Composable
private fun ShowProfessionalServiceList(
    modifier: Modifier = Modifier,
    user: ActorModel,
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
                    onServiceInfo = {
                        //Do nothing
                    },
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
fun LightPreview() {
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
            user = PROFESSIONALMODELFORTESTS
        )
    }
}