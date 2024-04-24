package com.vzkz.profinder.ui.profile.editprofile

import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.vzkz.profinder.core.Constants.DESCRIPTION
import com.vzkz.profinder.core.Constants.FIRSTNAME
import com.vzkz.profinder.core.Constants.LASTNAME
import com.vzkz.profinder.core.Constants.NICKNAME
import com.vzkz.profinder.core.Constants.PROFESSION
import com.vzkz.profinder.core.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.destinations.ProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.MyCircularProgressbar
import com.vzkz.profinder.ui.components.MyColumn
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
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        editProfileViewModel.onInit()
    }
    val state = editProfileViewModel.state
    if(state.success){
        Toast.makeText(context, stringResource(R.string.successful_modification), Toast.LENGTH_SHORT).show()
    }
    if (state.wholeScreenLoading) {
        MyCircularProgressbar()
    } else {
        var user: ActorModel? by remember { mutableStateOf(null) }
        user = editProfileViewModel.state.user
        var profilePhoto: Uri? by remember { mutableStateOf(null) }
        profilePhoto = user?.profilePhoto
        ScreenBody(
            user = user,
            error = editProfileViewModel.state.error,
            loading = state.loading,
            profilePhoto = profilePhoto,
            onUploadImage = { uriToUpload ->
                editProfileViewModel.onUploadPhoto(
                    uri = uriToUpload,
                    user = editProfileViewModel.state.user ?: ActorModel()
                )
            },
            onModifyUserData = { changedFields ->
                if (user != null)
                    editProfileViewModel.onModifyUserData(
                        uid = user!!.uid,
                        changedFields = changedFields
                    )
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
    loading: Boolean,
    profilePhoto: Uri?,
    onUploadImage: (Uri) -> Unit,
    onModifyUserData: (Map<String, Any>) -> Unit,
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


        var firstName by remember { mutableStateOf("") }
        var readOnlyFirstName by remember { mutableStateOf(true) }


        var lastName by remember { mutableStateOf("") }
        var readOnlyLastName by remember { mutableStateOf(true) }

        var description by remember { mutableStateOf("") }
        var readOnlyDescription by remember { mutableStateOf(true) }

        var profession: Professions? by remember { mutableStateOf(null) }

        var readOnlyActor by remember { mutableStateOf(true) }

        var firstTime by remember { mutableStateOf(true) }
        var showConfirmDialog by remember { mutableStateOf(false) }

        val spaceBetween = 8
        val defaultVal = "-"

        if (firstTime) {
            nickname = user?.nickname ?: defaultVal
            firstName = user?.firstname ?: defaultVal
            lastName = user?.lastname ?: defaultVal
            description = user?.description ?: defaultVal
            profession = user?.profession
            firstTime = false
        }

        IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = {
            onBackClicked()
        }, enabled = !loading) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Cancel"
            )
        }

        MyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        ) { //Body
            MyRow { //Profile Picture
                ProfilePicture(profilePhoto = profilePhoto, size = 80)
                MySpacer(size = 8)
                OutlinedButton(
                    enabled = !loading,
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
                        Icon(imageVector = if(readOnlyNickname) Icons.Filled.Edit else Icons.Filled.EditOff, contentDescription = "Edit Icon")
                    }
                }
            )
            MySpacer(size = spaceBetween)
            MyGenericTextField(
                modifier = Modifier,
                hint = stringResource(R.string.first_name),
                text = firstName,
                onTextChanged = { firstName = it },
                readOnly = readOnlyFirstName,
                trailingIcon = {
                    IconButton(onClick = { readOnlyFirstName = !readOnlyFirstName }) {
                        Icon(imageVector = if(readOnlyFirstName) Icons.Filled.Edit else Icons.Filled.EditOff, contentDescription = "Edit Icon")
                    }
                }
            )
            MySpacer(size = spaceBetween)
            MyGenericTextField(
                modifier = Modifier,
                hint = stringResource(R.string.last_name),
                text = lastName,
                onTextChanged = { lastName = it },
                readOnly = readOnlyLastName,
                trailingIcon = {
                    IconButton(onClick = { readOnlyLastName = !readOnlyLastName }) {
                        Icon(imageVector = if(readOnlyLastName) Icons.Filled.Edit else Icons.Filled.EditOff, contentDescription = "Edit Icon")
                    }
                }
            )
            if (user?.actor == Actors.Professional) {
                Box(modifier = Modifier) {
                    var expandedProfessionDropdownMenu by remember { mutableStateOf(false) }
                    Column {
                        OutlinedTextField(
                            value = profession?.name ?: "",
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
                    }
                    DropdownMenu(
                        modifier = Modifier,
                        expanded = expandedProfessionDropdownMenu,
                        onDismissRequest = { expandedProfessionDropdownMenu = false }) {
                        Professions.entries.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(it.name, style = MaterialTheme.typography.bodyMedium)
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
            }
            MySpacer(size = spaceBetween)
            MyGenericTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 96.dp),
                hint = stringResource(R.string.description),
                text = description,
                singleLine = false,
                onTextChanged = { description = it },
                readOnly = readOnlyDescription,
                trailingIcon = {
                    IconButton(onClick = { readOnlyDescription = !readOnlyDescription }) {
                        Icon(imageVector = if(readOnlyDescription) Icons.Filled.Edit else Icons.Filled.EditOff, contentDescription = "Edit Icon")
                    }
                }
            )
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
            if (loading)
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.background
                )
            else
                Text(text = stringResource(R.string.save))
        }

        MyConfirmDialog(
            title = stringResource(R.string.are_you_sure),
            text = stringResource(R.string.profile_changes_like_username_may_not_be_undoable),
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
                    val changedFields = getChangedFields(
                        originalUser = user,
                        nickname = nickname,
                        firstname = firstName,
                        lastname = lastName,
                        profession = profession,
                        description = description
                    )

                    if (changedFields.isNotEmpty())
                        onModifyUserData(changedFields)
                }
            },
            showDialog = showConfirmDialog
        )

        if (error != null) {
            MyAlertDialog(
                //Error Dialog
                title = stringResource(R.string.error_during_profile_modification),
                text = error.asString(),
                onDismiss = {
                    onCloseDialog()
                },
                onConfirm = {
                    onCloseDialog()
                },
            )
            firstTime = true
        }

        if (showPhotoDialog) {
            UploadPhotoDialog(
                onDismiss = {
                    showPhotoDialog = false
                },
                onCameraClicked = {
                    uri = generateUri(userTitle, context)
                    intentCameraLauncher.launch(uri!!)
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

fun getChangedFields(
    originalUser: ActorModel,
    nickname: String,
    firstname: String,
    lastname: String,
    profession: Professions?,
    description: String
): Map<String, Any> {
    val changedFields: MutableMap<String, Any> = mutableMapOf()
    if (nickname != originalUser.nickname)
        changedFields[NICKNAME] = nickname
    if (originalUser.firstname != firstname)
        changedFields[FIRSTNAME] = firstname
    if (originalUser.lastname != lastname)
        changedFields[LASTNAME] = lastname
    if (originalUser.actor == Actors.Professional && originalUser.profession == profession && profession != null)
        changedFields[PROFESSION] = profession.name
    if (originalUser.description != description)
        changedFields[DESCRIPTION] = description

    return changedFields
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    ProFinderTheme {
        ScreenBody(
            user = PROFESSIONALMODELFORTESTS,
            error = null,
//            loading = true,
            loading = false,
            onModifyUserData = { _ -> },
            onCloseDialog = {},
            onBackClicked = {},
            profilePhoto = null,
            onUploadImage = {}
        )
    }
}