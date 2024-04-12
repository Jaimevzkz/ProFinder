package com.vzkz.profinder.ui.services.components.userscreen

import androidx.annotation.StringRes
import com.vzkz.profinder.R

enum class ServiceState(@StringRes val id: Int) {
    FREE(R.string.request),
    REQUESTED(R.string.cancel_request),
    JOB(R.string.active)
}