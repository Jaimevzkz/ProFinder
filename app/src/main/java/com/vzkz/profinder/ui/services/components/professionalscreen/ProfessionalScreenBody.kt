package com.vzkz.profinder.ui.services.components.professionalscreen

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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.dialogs.AddServiceDialog
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.dialogs.MyConfirmDialog
import com.vzkz.profinder.ui.services.components.userscreen.ServiceCard

@Composable
fun ProfessionalScreenBody(
    modifier: Modifier = Modifier,
    user: ActorModel,
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
    servicesToShow: List<ServiceModel>,
    onChangeSidToDelete: (String) -> Unit,
    onConfirmDialogVisibilityChange: (Boolean) -> Unit,
    onActivityChange: (ServiceModel) -> Unit,
) {
    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    val cardContentColor = MaterialTheme.colorScheme.onSurfaceVariant

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