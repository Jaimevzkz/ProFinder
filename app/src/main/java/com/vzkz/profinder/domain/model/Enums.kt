package com.vzkz.profinder.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Plumbing
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
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
    val tint: Color,
    @StringRes val string: Int
){
    Active(active,R.string.active),
    Working(working, R.string.working),
    Inactive(inactive, R.string.inactive)
}
enum class Professions(
    val icon: ImageVector,
    @StringRes val string: Int
) {
    Plumber(icon = Icons.Outlined.Plumbing, R.string.plumber),
    Hairdresser(icon = Icons.Outlined.ContentCut,R.string.hairdresser),
    Electrician(icon = Icons.Outlined.ElectricalServices, R.string.electrician),
    Psychologist(icon = Icons.Outlined.Psychology, R.string.psychologist),
    Teacher(icon = Icons.Outlined.School, R.string.teacher),
    Accountant(icon = Icons.Outlined.Numbers, R.string.accountant),
    Lawyer(icon = Icons.Outlined.AccountBalance, R.string.lawyer)
}
enum class Categories(@StringRes val string: Int){
    Household(R.string.household),
    Beauty(R.string.beauty),
    Health(R.string.health),
    Education(R.string.education),
    Law(R.string.law),
    Economics(R.string.economics)
}