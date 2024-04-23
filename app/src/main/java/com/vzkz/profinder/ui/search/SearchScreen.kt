package com.vzkz.profinder.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R
import com.vzkz.profinder.core.PROFFESIONALLISTFORTEST
import com.vzkz.profinder.destinations.SearchScreenDestination
import com.vzkz.profinder.destinations.ViewProfileScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.ProfilePictureShimmer
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.components.shimmer.IconShimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        searchViewModel.onInit()
    }
    ScreenBody(
        error = searchViewModel.state.error,
        onCloseDialog = searchViewModel::onCloseDialog,
        state = searchViewModel.state,
        onBottomBarClicked = { navigator.navigate(it) },
        onSeeProfile = { navigator.navigate(ViewProfileScreenDestination(it)) }
    )
}

@Composable
private fun ScreenBody(
    error: UiText?,
    state: SearchState,
    onCloseDialog: () -> Unit,
    onSeeProfile: (String) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
) {
    MyBottomBarScaffold(
        currentDestination = SearchScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) }
    ) { paddingValues ->
        if (state.loading) {
            ProfileScreenShimmer()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(12.dp)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                var query by remember { mutableStateOf("") }
                var isUserChipSelected by remember { mutableStateOf(true) }
                var isProfessionalChipSelected by remember { mutableStateOf(true) }
                MyColumn(modifier = Modifier.align(Alignment.TopCenter)) {
                    MyGenericTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        hint = stringResource(R.string.type_a_user_to_search),
                        shape = MaterialTheme.shapes.large,
                        text = query,
                        onTextChanged = {
                            query = it
                        }
                    )
                    MySpacer(size = 8)
                    MyRow(
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        val backgroundColorSelected = MaterialTheme.colorScheme.onBackground
                        val fontColorSelected = MaterialTheme.colorScheme.background
                        val backgroundColorUnselected = MaterialTheme.colorScheme.surfaceVariant
                        val fontColorUnselected = MaterialTheme.colorScheme.onSurfaceVariant

                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isUserChipSelected) backgroundColorSelected else backgroundColorUnselected)
                            .clickable {
                                if ((isUserChipSelected && isProfessionalChipSelected) || !isUserChipSelected)
                                    isUserChipSelected = !isUserChipSelected
                            }
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                text = stringResource(id = R.string.users),
                                color = if (isUserChipSelected) fontColorSelected else fontColorUnselected,
                            )
                        }
                        MySpacer(size = 12)
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isProfessionalChipSelected) backgroundColorSelected else backgroundColorUnselected)
                            .clickable {
                                if ((isProfessionalChipSelected && isUserChipSelected) || !isProfessionalChipSelected)
                                    isProfessionalChipSelected = !isProfessionalChipSelected
                            }
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                text = stringResource(id = R.string.professionals),
                                color = if (isProfessionalChipSelected) fontColorSelected else fontColorUnselected,
                            )
                        }
                        MySpacer(size = 12)
                    }
                    MySpacer(size = 8)
                    LazyColumn {
                        items(state.userList.filter {
                            filterItem(
                                actor = it,
                                query = query,
                                isUserChipSelected = isUserChipSelected,
                                isProfessionalChipSelected = isProfessionalChipSelected
                            )
                        }) {
                            MySpacer(size = 8)
                            ProfileItem(actor = it, onClick = onSeeProfile)
                        }
                    }
                }


                if (error != null) {
                    MyAlertDialog(
                        title = stringResource(R.string.error),
                        text = error.asString(),
                        onDismiss = { onCloseDialog() },
                        onConfirm = { onCloseDialog() },
                    )
                }
            }

        }

    }
}

private fun filterItem(
    actor: ActorModel,
    query: String,
    isUserChipSelected: Boolean,
    isProfessionalChipSelected: Boolean
) =
    (actor.nickname.contains(query) || actor.firstname.lowercase().contains(query) || actor.lastname.lowercase().contains(
        query
    )) && (isUserChipSelected && actor.actor == Actors.User || isProfessionalChipSelected && actor.actor == Actors.Professional)

@Composable
fun ProfileItem(modifier: Modifier = Modifier, actor: ActorModel, onClick: (String) -> Unit) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { onClick(actor.uid) }) {
        ProfilePicture(
            profilePhoto = actor.profilePhoto,
            size = 65,
            shape = MaterialTheme.shapes.large
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Column {
            Text(text = "${actor.firstname} ${actor.lastname}", fontWeight = FontWeight.Bold)
            MySpacer(size = 4)
            MyRow {
                Text(text = actor.nickname, fontWeight = FontWeight.Light)
                MySpacer(size = 12)
                Text(
                    text = actor.actor.name,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ProfileScreenShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        MyColumn(Modifier.align(Alignment.TopCenter)) {
            Box(
                modifier = Modifier
                    .shimmer()
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = 12)
            Row {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(85.dp)
                        .height(35.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                MySpacer(size = 12)
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(110.dp)
                        .height(35.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            }
            MySpacer(size = 12)
            for (i in 1..10){
                MyRow(modifier = Modifier.fillMaxWidth()) {
                    ProfilePictureShimmer(size = 65, shape = MaterialTheme.shapes.large)
                    MySpacer(size = 12)
                    MyColumn {
                        Box(
                            modifier = Modifier
                                .shimmer()
                                .padding(top = 4.dp)
                                .width(170.dp)
                                .height(20.dp)
                                .background(Color.Gray)
                        )
                        MySpacer(size = 8)
                        MyRow {
                            Box(
                                modifier = Modifier
                                    .shimmer()
                                    .width(60.dp)
                                    .height(22.dp)
                                    .background(Color.Gray)
                            )
                            MySpacer(size = 12)
                            Box(
                                modifier = Modifier
                                    .shimmer()
                                    .clip(CircleShape)
                                    .width(100.dp)
                                    .height(30.dp)
                                    .background(Color.Gray)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconShimmer()
                }
                MySpacer(size = 12)
            }

        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            error = null,
            onCloseDialog = {},
            onBottomBarClicked = {},
            state = SearchState.initial.copy(userList = PROFFESIONALLISTFORTEST, loading = false),
            onSeeProfile = {}
        )
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShimmerPreview() {
    ProFinderTheme {
        ScreenBody(
            error = null,
            onCloseDialog = {},
            onBottomBarClicked = {},
            state = SearchState.initial.copy(userList = PROFFESIONALLISTFORTEST, loading = true),
            onSeeProfile = {}
        )
    }
}
