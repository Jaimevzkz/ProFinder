package com.vzkz.profinder.ui.splash

sealed class SplashDestinations {
    data object HomeDest: SplashDestinations()
    data object LoginDest: SplashDestinations()
}