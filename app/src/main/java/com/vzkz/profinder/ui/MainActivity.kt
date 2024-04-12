package com.vzkz.profinder.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.vzkz.profinder.NavGraphs
import com.vzkz.profinder.domain.usecases.ThemeDSUseCase
import com.vzkz.profinder.ui.theme.ProFinderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeDSUseCase: ThemeDSUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        var flow: Flow<Boolean> = MutableStateFlow(false)
        CoroutineScope(Dispatchers.IO).launch {
            flow = themeDSUseCase()
        }
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            LaunchedEffect(flow) {
                flow.collect { value ->
                    darkTheme = value
                }
            }
            ProFinderTheme(darkTheme = darkTheme) {
                // A surface container using the 'backgThere are a lot of ways to configure this destination. Lets start with an example where all those are used:There are a lot of ways to configure this destination. Lets start with an example where all those are used:round' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}