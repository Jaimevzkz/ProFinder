package com.vzkz.profinder.ui.components.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.ChatScreenDestination
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.destinations.SearchScreenDestination
import com.vzkz.profinder.destinations.ServicesScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    HomeDest(HomeScreenDestination, Icons.Outlined.Home, R.string.home),
    ServicesDest(ServicesScreenDestination, Icons.Outlined.Handshake, R.string.services),
    SearchDest(SearchScreenDestination, Icons.Outlined.Search, R.string.search),
    ChatDest(ChatScreenDestination, Icons.AutoMirrored.Outlined.Chat, R.string.chat),
    ProfileDest(ProfileScreenDestination, Icons.Outlined.Person, R.string.profile)
}