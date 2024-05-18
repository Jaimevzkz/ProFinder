package com.vzkz.profinder.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.vzkz.profinder.destinations.LoginScreenDestination
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.ui.components.MyAuthHeader
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MyEmailTextField
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MyImageLogo
import com.vzkz.profinder.ui.components.MyPasswordTextField
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.validateEmail
import com.vzkz.profinder.ui.components.validatePassword
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val state = signUpViewModel.state
    if (state.success) {
        navigator.navigate(HomeScreenDestination)
    } else if (state.loading) {
        MyCircularProgressbar(backGroundColor = MaterialTheme.colorScheme.background)
    } else {
        ScreenBody(
            onSignInClicked = {
                navigator.navigate(LoginScreenDestination)
            },
            onSignUp = { email, password, nickname, firstname, lastname, actor, profession ->
                signUpViewModel.onSignUp(
                    email = email,
                    password = password,
                    nickname = nickname,
                    firstname = firstname,
                    lastname = lastname,
                    actor = actor,
                    profession = profession
                )
            },
            state = state,
            onCloseDialog = { signUpViewModel.onCloseDialog() },
        )
    }
}

@Composable
private fun ScreenBody(
    onSignInClicked: () -> Unit,
    onSignUp: (String, String, String, String, String, Actors, Professions?) -> Unit,
    onCloseDialog: () -> Unit,
    state: SignUpState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        var actorType: Actors by remember { mutableStateOf(Actors.Professional) }
        var profession: Professions? by remember { mutableStateOf(null) }
        //validation
        var enableSignup by remember { mutableStateOf(false) }
        var isEmailValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isSamePassword by remember { mutableStateOf(true) }
        var isFirstnameValid by remember { mutableStateOf(true) }
        var isLastnameValid by remember { mutableStateOf(true) }
        var isProfessionValid by remember { mutableStateOf(true) }

        if(profession != null) isProfessionValid = true
        if(actorType == Actors.User) profession = null
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp, end = 12.dp)
        ) {
            MyImageLogo()
            MySpacer(16)
            MyEmailTextField(modifier = Modifier, text = email, onTextChanged = {
                isEmailValid = validateEmail(it)
                enableSignup = true
                email = it
            })
            if (!isEmailValid) {
                Text(
                    text = stringResource(R.string.email_must_have_a_valid_format),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyPasswordTextField(
                modifier = Modifier,
                text = password,
                hint = stringResource(R.string.password),
                onTextChanged = {
                    isPasswordValid = validatePassword(it)
                    password = it
                })
            if (!isPasswordValid) {
                Text(
                    text = stringResource(R.string.invalid_password),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyPasswordTextField(
                modifier = Modifier,
                text = repeatPassword,
                hint = stringResource(R.string.repeat_password),
                onTextChanged = {
                    repeatPassword = it
                    isSamePassword = repeatPassword == password
                })
            if (!isSamePassword) {
                Text(
                    text = stringResource(R.string.passwords_must_coincide),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)

            MyGenericTextField(
                modifier = Modifier,
                text = nickname,
                hint = stringResource(R.string.nickname),
                onTextChanged = { nickname = it }
            )
            MySpacer(size = 8)

            MyGenericTextField(
                modifier = Modifier,
                text = firstname,
                hint = stringResource(R.string.first_name),
                onTextChanged = {
                    firstname = it
                    isFirstnameValid = firstname.isNotEmpty()
                }
            )
            if (!isFirstnameValid) {
                Text(
                    text = stringResource(R.string.first_name_should_not_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)

            MyGenericTextField(
                modifier = Modifier,
                text = lastname,
                hint = stringResource(R.string.last_name),
                onTextChanged = {
                    lastname = it
                    isLastnameValid = lastname.isNotEmpty()
                }
            )

            if (!isLastnameValid) {
                Text(
                    text = stringResource(R.string.last_name_should_not_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            Column {// In order to align login button at end
                Box(modifier = Modifier) {
                    var expandedActorDropdownMenu by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = stringResource(id = actorType.string),
                        onValueChange = {/*Only read*/ },
                        label = { Text(stringResource(R.string.user_professional)) },
                        leadingIcon = {
                            IconButton(onClick = { expandedActorDropdownMenu = true }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier,
                        expanded = expandedActorDropdownMenu,
                        onDismissRequest = { expandedActorDropdownMenu = false }) {
                        Actors.entries.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(id = it.string), style = MaterialTheme.typography.bodyMedium)
                                },
                                onClick = {
                                    actorType = it
                                    expandedActorDropdownMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        it.icon,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
                MySpacer(size = 8)
                if(actorType == Actors.Professional){
                    Box(modifier = Modifier) {
                        var expandedProfessionDropdownMenu by remember { mutableStateOf(false) }
                        Column {
                            OutlinedTextField(
                                value = profession?.let { it1 -> stringResource(id = it1.string) } ?: "",
                                onValueChange = {/*Only read*/ },
                                label = { Text(stringResource(R.string.profession)) },
                                leadingIcon = {
                                    IconButton(onClick = { expandedProfessionDropdownMenu = true }) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowDropDown,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                            if(!isProfessionValid){
                                Text(
                                    text = stringResource(R.string.choose_a_profession_to_continue),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        DropdownMenu(
                            modifier = Modifier,
                            expanded = expandedProfessionDropdownMenu,
                            onDismissRequest = { expandedProfessionDropdownMenu = false }) {
                            Professions.entries.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(id = it.string), style = MaterialTheme.typography.bodyMedium)
                                    },
                                    onClick = {
                                        profession = it
                                        expandedProfessionDropdownMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = it.icon,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                    MySpacer(8)
                }
                Text(
                    text = stringResource(R.string.login),
                    Modifier
                        .clickable { onSignInClicked() }
                        .align(Alignment.End),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Button(
            onClick = {
                if(actorType == Actors.Professional && profession ==  null){
                    isProfessionValid = false
                } else{
                    isProfessionValid = true
                    if (enableSignup && isEmailValid && isPasswordValid && isSamePassword && isFirstnameValid && isLastnameValid) {
                        onSignUp(email, password, nickname, firstname, lastname, actorType, profession)
                    }
                }
            },
            Modifier
                .fillMaxWidth()
                .padding(48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.signup))
        }

        MyAuthHeader(Modifier.align(Alignment.TopEnd))

        if(state.error != null){
            MyAlertDialog(
                title = stringResource(R.string.error_during_sign_up),
                text = state.error.asString(),
                onDismiss = { onCloseDialog() },
                onConfirm = { onCloseDialog() },
            )
        }
    }
}

@Preview
@Composable
fun SignUpPreview() {
    ProFinderTheme {
        ScreenBody(
            onSignInClicked = { },
            onSignUp = { _, _, _, _, _, _, _ -> },
            onCloseDialog = { },
            state = SignUpState.initial
        )
    }
}