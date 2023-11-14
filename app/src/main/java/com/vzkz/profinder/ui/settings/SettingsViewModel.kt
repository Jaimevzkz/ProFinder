package com.vzkz.profinder.ui.settings

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.usecases.ThemeDSUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(private val themeDSUseCase: ThemeDSUseCase) :
    BaseViewModel<SettingsState, SettingsIntent>(SettingsState.initial) {

    override fun reduce(
        state: SettingsState,
        intent: SettingsIntent
    ): SettingsState { //This function reduces each intent with a when
        return when (intent) {
            is SettingsIntent.Theme -> state.copy(darkTheme = intent.isDark)
            is SettingsIntent.Init -> TODO()
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            dispatch(SettingsIntent.Init(themeDSUseCase.switchTheme()))
        }
    }
    fun onThemeSwitch() {
        viewModelScope.launch(Dispatchers.IO) {
            dispatch(SettingsIntent.Theme(themeDSUseCase.switchTheme()))
        }
    }


}