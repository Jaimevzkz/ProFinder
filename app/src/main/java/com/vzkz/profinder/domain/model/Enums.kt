package com.vzkz.profinder.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Plumbing
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.vzkz.profinder.R
import com.vzkz.profinder.ui.theme.active
import com.vzkz.profinder.ui.theme.inactive
import com.vzkz.profinder.ui.theme.working

enum class Actors(
    val icon: ImageVector,
    @StringRes val string: Int
) {
    User(icon = Icons.Outlined.Person, R.string.user),
    Professional(icon = Icons.Outlined.Engineering, R.string.professional)
}
enum class ProfState(
    val tint: Color
){
    Active(active),
    Working(working),
    Inactive(inactive)
}
enum class Professions(
    val icon: ImageVector,
) {
    Plumber(icon = Icons.Outlined.Plumbing),
    Hairdresser(icon = Icons.Outlined.ContentCut),
    Electrician(icon = Icons.Outlined.ElectricalServices)
}
enum class Categories{
    Household,
    Beauty
}