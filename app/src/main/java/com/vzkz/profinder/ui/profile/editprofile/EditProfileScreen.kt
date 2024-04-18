package com.vzkz.profinder.ui.profile.editprofile

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.R
import com.vzkz.profinder.core.USERMODELFORTESTS
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.UploadPhotoDialog
import com.vzkz.profinder.ui.components.cameraIntent
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.dialogs.MyConfirmDialog
import com.vzkz.profinder.ui.components.galleryIntent
import com.vzkz.profinder.ui.components.generateUri
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun EditProfileScreen(
    navigator: DestinationsNavigator,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        editProfileViewModel.onInit()
    }
    val state = editProfileViewModel.state
    val loading = state.loading
    if (state.success) {
        navigator.navigate(ProfileScreenDestination)
    } else if (loading) {
        MyCircularProgressbar()
    } else {
        var user: ActorModel? by remember { mutableStateOf(null) }
        user = editProfileViewModel.state.user
        var profilePhoto: Uri? by remember { mutableStateOf(null) }
        profilePhoto = user?.profilePhoto
        ScreenBody(
            user = user,
            error = editProfileViewModel.state.error,
            profilePhoto = profilePhoto,
            onUploadImage = { uriToUpload ->
                editProfileViewModel.onUploadPhoto(
                    uri = uriToUpload,
                    user = editProfileViewModel.state.user ?: ActorModel()
                )
            },
            onModifyUserData = { newUser, oldUser ->
                editProfileViewModel.onModifyUserData(newUser = newUser, oldUser = oldUser)
            },
            onCloseDialog = { editProfileViewModel.onCloseDialog() },
            onBackClicked = { navigator.navigate(ProfileScreenDestination) }
        )
    }
}

@Composable
private fun ScreenBody(
    user: ActorModel?,
    error: UiText?,
    profilePhoto: Uri?,
    onUploadImage: (Uri) -> Unit,
    onModifyUserData: (ActorModel, ActorModel) -> Unit,
    onCloseDialog: () -> Unit,
    onBackClicked: () -> Unit
) {
    //Camera
    val context = LocalContext.current
    var showPhotoDialog by remember { mutableStateOf(false) }
    val userTitle: String by remember { mutableStateOf("") }
    var uri: Uri? by remember { mutableStateOf(null) }

    val intentCameraLauncher = cameraIntent(uri, onUploadImage)

    val intentGalleryLauncher = galleryIntent(onUploadImage)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        var nickname by remember { mutableStateOf("") }
        var readOnlyNickname by remember { mutableStateOf(true) }

        var actor by remember { mutableStateOf("") }
        var readOnlyActor by remember { mutableStateOf(true) }

        var firstTime by remember { mutableStateOf(true) }
        var showConfirmDialog by remember { mutableStateOf(false) }

        val spaceBetween = 8

        if (firstTime) {
            nickname = user?.nickname ?: ""
            actor = user?.actor?.name ?: ""
            firstTime = false
        }

        IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = {
            onBackClicked()
        }) {
            Icon(imageVector = Icons.Outlined.ArrowBackIosNew, tint = MaterialTheme.colorScheme.onBackground, contentDescription = "Cancel")
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        ) { //Body
            MyRow { //Profile Picture
                ProfilePicture(profilePhoto = profilePhoto, size = 80)
                MySpacer(size = 8)
                OutlinedButton(
                    onClick = { showPhotoDialog = true },
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.change_profile_photo))
                }
            }

            MySpacer(size = spaceBetween)
            MyGenericTextField(
                modifier = Modifier,
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
            MySpacer(size = 8)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onClick = {
                showConfirmDialog = true
            }
        ) {
            Text(text = stringResource(R.string.save))
        }

        MyConfirmDialog(
            title = "Are you sure?",
            text = "Profile changes like username may not be undoable",
            onDismiss = {
                readOnlyNickname = true
                readOnlyActor = true
                showConfirmDialog = false
            },
            onConfirm = {
                readOnlyNickname = true
                readOnlyActor = true
                showConfirmDialog = false
                if (user != null) {
                    val newUser = user
//                    val newUser = user.copy(nickname = nickname) //TODO Refactor this to be scalable
                    onModifyUserData(newUser, user) //We know the user is not null
                }
            },
            showDialog = showConfirmDialog
        )

        if(error != null){
            MyAlertDialog( //Error Dialog
                title = stringResource(R.string.error_during_profile_modification),
                text = error.asString(),
                onDismiss = {
                    onCloseDialog()
                },
                onConfirm = {
                    onCloseDialog()
                },
            )
        }

        if (showPhotoDialog) {
            UploadPhotoDialog(
                onDismiss = {
                    showPhotoDialog = false
                },
                onCameraClicked = {
                    uri = generateUri(userTitle, context)
                    intentCameraLauncher.launch(uri)
                    showPhotoDialog = false
                },
                onGalleryClicked = {
                    intentGalleryLauncher.launch("image/*")
                    showPhotoDialog = false
                }
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    ProFinderTheme {
        ScreenBody(
            user = USERMODELFORTESTS,
            error = null,
            onModifyUserData = { _, _ -> },
            onCloseDialog = {},
            onBackClicked = {},
            profilePhoto = null,
            onUploadImage = {}
        )
    }
}