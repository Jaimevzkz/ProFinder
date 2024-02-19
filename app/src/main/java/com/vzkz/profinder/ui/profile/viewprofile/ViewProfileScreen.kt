package com.vzkz.profinder.ui.profile.viewprofile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun ViewProfileScreen(
    navigator: DestinationsNavigator,
    viewProfileViewModel: ViewProfileViewModel = hiltViewModel(),
    uidToSee: String
) {
    viewProfileViewModel.onInit(uidToSee)
    val error = viewProfileViewModel.state.error
    var userToSee: ActorModel? by remember { mutableStateOf(null) }
    userToSee = viewProfileViewModel.state.userToSee
    val isFavourite = viewProfileViewModel.state.isFavourite
    ScreenBody(
        userToSee = userToSee ?: ActorModel(),
        error = error,
        isFavourite = isFavourite,
        onChangeFavourite = {
            viewProfileViewModel.onFavouriteChanged(uidToChange = uidToSee, add = !isFavourite)
        },
        onCloseDialog = {
            viewProfileViewModel.onCloseDialog()
        },
        onNavBack = {
            navigator.popBackStack()
        }
    )
}

@Composable
private fun ScreenBody(
    userToSee: ActorModel,
    error: UiError,
    isFavourite: Boolean,
    onChangeFavourite: () -> Unit,
    onCloseDialog: () -> Unit,
    onNavBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val cardColor = MaterialTheme.colorScheme.surfaceVariant
        val cardContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val defaultVal = "- "
        IconButton(
            onClick = { onNavBack() }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Navigate back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 20.dp)
        ) {
            //Header
            MySpacer(size = 32)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture(
                    modifier = Modifier,
                    profilePhoto = userToSee.profilePhoto,
                    size = 100
                )
                MySpacer(size = 4)
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = userToSee.firstname,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userToSee.lastname,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            //Status
            if (userToSee.actor == Actors.Professional) {
                //State
                MySpacer(size = 8)
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    IconButton(onClick = {
                        onChangeFavourite()
                    }, modifier = Modifier) {
                        Icon(
                            imageVector = if (!isFavourite) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                            contentDescription = "Navigate back",
                            tint = if (!isFavourite) MaterialTheme.colorScheme.onBackground else Color.Red
                        )
                    }
                    MySpacer(size = 8)
                    Row(
                        modifier = Modifier
                            .shadow(elevation = 10.dp, shape = RoundedCornerShape(40))
                            .background(cardColor)
                            .padding(20.dp)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {//Profession and state should never be null
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dot),
                            contentDescription = null,
                            tint = userToSee.state?.tint ?: Color.Transparent,
                            modifier = Modifier
                                .size(20.dp),
                        )
                        MySpacer(size = 16)
                        Text(
                            text = userToSee.profession!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = cardContentColor
                        )
                    }
                }
            }
            MySpacer(size = 16)

            //profile_details
            Column(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(10))
                    .background(cardColor)
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val spaceBetween = 14
                val innerSpaceBetween = 6
                MySpacer(size = spaceBetween)
                Text(
                    text = stringResource(R.string.nickname),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = cardContentColor
                )
                MySpacer(size = innerSpaceBetween)
                Text(
                    text = userToSee.nickname, style = MaterialTheme.typography.titleLarge,
                    color = cardContentColor
                )
                MySpacer(size = spaceBetween)
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = cardContentColor
                )
                MySpacer(size = innerSpaceBetween)
                Text(
                    text = userToSee.description ?: defaultVal,
                    style = MaterialTheme.typography.titleLarge,
                    color = cardContentColor
                )
            }
        }


        MyAlertDialog(
            title = stringResource(R.string.error),
            text = error.errorMsg.orEmpty(),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = error.isError
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            userToSee = PROFESSIONALMODELFORTESTS,
            error = UiError(false, "Account wasn't created"),
            isFavourite = true,
            onChangeFavourite = {},
            onCloseDialog = {},
            onNavBack = {}
        )
    }

}
