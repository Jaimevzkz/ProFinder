package com.vzkz.profinder.ui.services.components.userscreen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.auth.api.phone.SmsCodeAutofillClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.SERVICELISTFORTEST
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.dialogs.ServiceDetailsDialog
import com.vzkz.profinder.ui.services.components.ServiceCard
import com.vzkz.profinder.ui.theme.ProFinderTheme
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserScreenBody(
    modifier: Modifier = Modifier,
    serviceList: List<ServiceModel>,
    onSeeProfile: (ActorModel) -> Unit,
    onSeeMap: () -> Unit
) {

    var query by remember { mutableStateOf("") }
    var showServiceInfo by remember { mutableStateOf(false) }
    var serviceToShow: ServiceModel? by remember { mutableStateOf(null) }

    //
//    //Location
//    val context = LocalContext.current
//    var location by remember { mutableStateOf<Location?>(null) }
//    val fusedLocationClient: FusedLocationProviderClient =
//        LocationServices.getFusedLocationProviderClient(context)
//
//    // Request location permission
//    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
//
//    // Retrieve the last known location
//    LaunchedEffect(key1 = permissionState) {
//        if (
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                context as Activity,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                1
//            )
//        } else {
//            location = fusedLocationClient.lastLocation.await()
//        }
//    }

    //
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
                /*onSeeMap()*/

//                permissionState.launchPermissionRequest()
//
//                Log.i("Jaime", "Location: $location")
//                Log.i("Permission state", "Permission: ${permissionState.status.isGranted}")
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
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                fontColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onSeeProfile = { if (serviceToShow != null) onSeeProfile(serviceToShow!!.owner) },
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
@Preview
fun UserScreenBodyPreview() {
    ProFinderTheme {
        UserScreenBody(
            serviceList = SERVICELISTFORTEST,
            onSeeProfile = {},
            onSeeMap = {}
        )
    }
}