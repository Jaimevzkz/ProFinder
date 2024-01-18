package com.vzkz.profinder.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Actors(
    val icon: ImageVector
) {
    User(icon = Icons.Outlined.Person),
    Professional(icon = Icons.Outlined.Engineering)
}

enum class ProfState{
    Active,
    Working,
    Inactive
}

enum class Professions{
    Plumber,
    Hairdresser,
    Electrician
}