package com.vzkz.profinder.ui.profile.editprofile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MySpacer

@Destination
@Composable
fun EditProfileScreen(
    navigator: DestinationsNavigator,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {
    editProfileViewModel.onInit()
    ScreenBody(editProfileViewModel){
        navigator.navigate(ProfileScreenDestination)
    }
}

@Composable
private fun ScreenBody(editProfileViewModel: EditProfileViewModel = hiltViewModel(), onProfileSavedOrCanccelled: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        var nickname by remember { mutableStateOf("") }
        var readOnlyNickname by remember { mutableStateOf(true) }
        var firstTime by remember { mutableStateOf(true) }
        if (firstTime || nickname == "") {
            nickname = editProfileViewModel.state.user?.nickname ?: ""
            firstTime = false
        }
        MyGenericTextField(
            modifier = Modifier.align(Alignment.TopCenter),
            hint = stringResource(R.string.nickname),
            text = nickname,
            onTextChanged = { nickname = it },
            readOnly = readOnlyNickname,
            trailingIcon = {
                IconButton(onClick = { readOnlyNickname = !readOnlyNickname }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Icon")
                }
            }
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (editProfileViewModel.state.user != null) {
                    val newUser = editProfileViewModel.state.user!!.copy(nickname = nickname)
                    editProfileViewModel.onModifyUserData(
                        newUser,
                        editProfileViewModel.state.user!!
                    ) //We know the user is not null
                    onProfileSavedOrCanccelled()
                }
            }) {
                Text(text = stringResource(R.string.save))
            }
            MySpacer(size = 16)
            Button(onClick = {
                nickname = ""
                readOnlyNickname = true
                firstTime = true
                onProfileSavedOrCanccelled()
            }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}

@Preview
@Composable
fun LightPreview() {
    ScreenBody(){}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {

}