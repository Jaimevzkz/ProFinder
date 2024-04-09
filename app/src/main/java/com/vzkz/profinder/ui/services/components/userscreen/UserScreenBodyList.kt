package com.vzkz.profinder.ui.services.components.userscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.vzkz.profinder.R
import com.vzkz.profinder.core.SERVICELISTFORTEST
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.dialogs.ServiceDetailsDialog
import com.vzkz.profinder.ui.services.components.ServiceCard
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun UserScreenBody(
    modifier: Modifier = Modifier,
    requestExists: Boolean,
    serviceList: List<ServiceModel>,
    onCheckRequestExists: (String) -> Unit,
    onCancelRequest: (String) -> Unit,
    onSeeProfile: (ActorModel) -> Unit,
    onRequestService: (ServiceModel) -> Unit,
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
                            onCheckRequestExists(serviceModel.sid)
                            serviceToShow = serviceModel
                            showServiceInfo = true
                        }
                    )
                    MySpacer(size = 16)
                }
            }

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .padding(bottom = 20.dp)
                .shadow(1.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            onClick = {
                onSeeMap()
            }
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
                requestExists = requestExists,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                fontColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onSeeProfile = { if (serviceToShow != null) onSeeProfile(serviceToShow!!.owner) },
                onRequest = { onRequestService(serviceToShow!!) },
                onCancelRequest = onCancelRequest,
                onCloseDialog = {
                    serviceToShow = null
                    showServiceInfo = false
                }
            )
        }
    }

}

@Composable
@Preview
fun UserScreenBodyPreview() {
    ProFinderTheme {
        UserScreenBody(
            serviceList = SERVICELISTFORTEST,
            requestExists = false,
            onSeeProfile = {},
            onSeeMap = {},
            onRequestService = {},
            onCheckRequestExists = {},
            onCancelRequest = {}
        )
    }
}