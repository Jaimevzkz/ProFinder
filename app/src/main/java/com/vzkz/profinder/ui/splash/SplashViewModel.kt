package com.vzkz.profinder.ui.splash

import androidx.lifecycle.ViewModel
import com.vzkz.profinder.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    //This viewmodel doesn't implement MVI because of the simplicity of the code
    fun getDestination(): SplashDestinations =
        if (repository.isUserLogged()) SplashDestinations.HomeDest else SplashDestinations.LoginDest
}


