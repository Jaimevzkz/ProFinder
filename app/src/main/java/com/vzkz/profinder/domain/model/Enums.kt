package com.vzkz.profinder.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Plumbing
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.vzkz.profinder.ui.theme.active
import com.vzkz.profinder.ui.theme.inactive
import com.vzkz.profinder.ui.theme.working

enum class Actors(
    val icon: ImageVector
) {
    User(icon = Icons.Outlined.Person),
    Professional(icon = Icons.Outlined.Engineering)
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
    category: Categories
) {
    Plumber(icon = Icons.Outlined.Plumbing, category = Categories.Household),
    Hairdresser(icon = Icons.Outlined.ContentCut, category = Categories.Beauty),
    Electrician(icon = Icons.Outlined.ElectricalServices, category = Categories.Household)
}
enum class Categories{
    Household,
    Beauty
}