package com.vzkz.profinder.ui.components.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.ChatScreenDestination
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.MapScreenDestination
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.destinations.SplashScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    HomeDest(HomeScreenDestination, Icons.Outlined.Home, R.string.home),
    MapDest(MapScreenDestination, Icons.Outlined.Map, R.string.map),
    ChatDest(ChatScreenDestination, Icons.Outlined.Chat, R.string.chat),
    ProfileDest(ProfileScreenDestination, Icons.Outlined.Person, R.string.profile)
}