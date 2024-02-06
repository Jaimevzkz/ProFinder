package com.vzkz.profinder.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.R
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.destinations.SignUpScreenDestination
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MyAuthHeader
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MyEmailTextField
import com.vzkz.profinder.ui.components.MyImageLogo
import com.vzkz.profinder.ui.components.MyPasswordTextField
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.validateEmail
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val state = loginViewModel.state
    if (state.success) {
        navigator.navigate(HomeScreenDestination)
    } else if (state.loading) {
        MyCircularProgressbar()
    } else {
        val error = state.error
        ScreenBody(
            onCloseDialog = { loginViewModel.onCloseDialog() },
            onLogin = { email, password ->
                loginViewModel.onLogin(email = email, password = password)
            },
            onSignUpClicked = {
                navigator.navigate(SignUpScreenDestination)
            },
            error = error
        )
    }
}

@Composable
private fun ScreenBody(
    onLogin: (String, String) -> Unit,
    onSignUpClicked: () -> Unit,
    onCloseDialog: () -> Unit,
    error: UiError
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {

        var email by remember { mutableStateOf("jaimevzkz1@gmail.com") } //todo delete
        var password by remember { mutableStateOf("1234Qwerty") }
        var isValid by remember { mutableStateOf(true) }

        MyAuthHeader(Modifier.align(Alignment.TopEnd))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-70).dp)
        ) {
            MyImageLogo()
            MySpacer(16)
            MyEmailTextField(modifier = Modifier, text = email, onTextChanged = {
                email = it
                isValid = validateEmail(email)
            })
            if (!isValid) {
                Text(
                    text = stringResource(R.string.email_must_have_a_valid_format),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            Column {
                MyPasswordTextField(
                    modifier = Modifier,
                    text = password,
                    hint = stringResource(R.string.password),
                    onTextChanged = { password = it })
                MySpacer(8)
                Text(
                    text = stringResource(R.string.signup),
                    Modifier
                        .clickable { onSignUpClicked() }
                        .align(Alignment.End),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Button(
            onClick = {
                if (isValid) {
                    onLogin(email, password)
                }
            },
            Modifier
                .fillMaxWidth()
                .padding(48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        MyAlertDialog(
            title = stringResource(R.string.error_during_login),
            text = error.errorMsg ?: stringResource(R.string.invalid_password),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = error.isError
        )
    }
}

@Preview
@Composable
fun LoginPreview() {
    ProFinderTheme {
        ScreenBody(
            onLogin = { _, _ -> },
            onSignUpClicked = {},
            onCloseDialog = {},
            error = UiError(false, "")
        )
    }
}