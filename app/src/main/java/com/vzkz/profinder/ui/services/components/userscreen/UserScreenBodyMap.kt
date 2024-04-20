package com.vzkz.profinder.ui.services.components.userscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R
import com.vzkz.profinder.data.hasLocationPermission
import com.vzkz.profinder.domain.model.LocationModel
import com.vzkz.profinder.ui.components.LocationBubbleShape
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog

@Composable
fun MapScreenBody(
    modifier: Modifier = Modifier,
    location: LatLng?,
    otherLocations: List<LocationModel>,
    onSeeList: () -> Unit
) {
    if (location == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .shimmer()
                    .fillMaxSize()
                    .background(Color.Gray)
            )
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
    } else {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            val library = LatLng(40.523356, -3.891714)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(location, 16f)
            }
            val mapProperties = MapProperties(
                isBuildingEnabled = false,
                isIndoorEnabled = false,
                isMyLocationEnabled = LocalContext.current.hasLocationPermission(),
                isTrafficEnabled = false,
                latLngBoundsForCameraTarget = null,
                mapStyleOptions = null,
                mapType = MapType.TERRAIN,
                maxZoomPreference = 21.0f,
                minZoomPreference = 3.0f,
            )

            val mapUiSettings = MapUiSettings(
                compassEnabled = true,
                indoorLevelPickerEnabled = true,
                mapToolbarEnabled = true,
                myLocationButtonEnabled = true,
                rotationGesturesEnabled = true,
                scrollGesturesEnabled = true,
                scrollGesturesEnabledDuringRotateOrZoom = true,
                tiltGesturesEnabled = true,
                zoomControlsEnabled = false,
                zoomGesturesEnabled = true,
            )
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {
                otherLocations.forEach { locationModel ->
                    MarkerComposable(
                        state = MarkerState(locationModel.location),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(LocationBubbleShape())
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            MyColumn(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .padding(bottom = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Engineering,
                                    contentDescription = null
                                )
                                Text(
                                    text = locationModel.nickname,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
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
                onClick = { onSeeList() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.FormatListNumbered,
                    contentDescription = "Show list view",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }


        }
        if (!LocalContext.current.hasLocationPermission()) {
            MyAlertDialog(
                title = stringResource(R.string.error),
                text = "Location permission is required for the map functionality to work. Grant it in settings and try again.",
                onDismiss = { onSeeList() },
                onConfirm = { onSeeList() },
            )
        }
    }
}

