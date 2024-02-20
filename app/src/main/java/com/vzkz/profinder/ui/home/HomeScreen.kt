package com.vzkz.profinder.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.PROFFESIONALLISTFORTEST
import com.vzkz.profinder.destinations.HomeScreenDestination
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.components.HomeCard
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, homeViewModel: HomeViewModel = hiltViewModel()) {
    homeViewModel.onInit()
    val error = homeViewModel.state.error
    var favList: List<ActorModel> by remember { mutableStateOf(emptyList()) }
    favList = homeViewModel.state.favList
    ScreenBody(
        favList = favList,
        error = error,
        onDeleteFav = { homeViewModel.onDeleteFav(it) },
        onCloseDialog = {
            homeViewModel.onCloseDialog()
        },
        onBottomBarClicked = { navigator.navigate(it) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    favList: List<ActorModel>,
    error: UiError,
    onDeleteFav: (String) -> Unit,
    onCloseDialog: () -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    MyBottomBarScaffold(
        currentDestination = HomeScreenDestination,
        onBottomBarClicked = { onBottomBarClicked(it) }
    ) { paddingValues ->
        val cardColor = MaterialTheme.colorScheme.surfaceVariant
        val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val fontFamily = FontFamily(Font(R.font.oswald))
        val cardPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        val contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
        var editFavList by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            MyColumn {
                HomeCard( //Hired Services
                    modifier = Modifier.weight(1f),
                    boxModifier = Modifier.padding(top = 8.dp),
                    cardColor = cardColor,
                    contentColor = contentColor,
                    fontFamily = fontFamily,
                    cardPadding = cardPadding,
                    contentPadding = contentPadding,
                    title = "Hired Services",
                    placeRight = false
                ) {

                }

                HomeCard( //Favorites
                    modifier = Modifier.weight(1f),
                    boxModifier = Modifier,
                    cardColor = cardColor,
                    contentColor = contentColor,
                    fontFamily = fontFamily,
                    cardPadding = cardPadding,
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    editFavList = editFavList,
                    onEditFavList = { editFavList = !editFavList },
                    title = "Favorites",
                    placeRight = true
                ) {
                    LazyColumn {
                        items(favList) { actor ->
                            MyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                ProfilePicture(profilePhoto = actor.profilePhoto, size = 50)
                                MySpacer(size = 12)
                                MyColumn {
                                    Text(
                                        text = "${actor.firstname} ${actor.lastname}",
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentColor
                                    )
                                    MyRow {
                                        Text(text = actor.nickname, color = contentColor)
                                        MySpacer(size = 4)
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_dot),
                                            contentDescription = null,
                                            tint = actor.state?.tint ?: Color.Transparent,
                                            modifier = Modifier
                                                .size(20.dp),
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (editFavList) {
                                    IconButton(onClick = { onDeleteFav(actor.uid) }) {
                                        Icon(
                                            imageVector = Icons.Filled.DeleteOutline,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            }
                            MySpacer(size = 12)
                        }
                    }
                }

                HomeCard( //Recent
                    modifier = Modifier.weight(1f),
                    boxModifier = Modifier.padding(bottom = 8.dp),
                    cardColor = cardColor,
                    contentColor = contentColor,
                    fontFamily = fontFamily,
                    cardPadding = cardPadding,
                    contentPadding = contentPadding,
                    title = "Recent",
                    placeRight = false
                ) {

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

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    ProFinderTheme {
        ScreenBody(
            favList = PROFFESIONALLISTFORTEST,
            error = UiError(false, "Account wasn't created"),
            onDeleteFav = {},
            onCloseDialog = {},
            onBottomBarClicked = {}
        )
    }

}
