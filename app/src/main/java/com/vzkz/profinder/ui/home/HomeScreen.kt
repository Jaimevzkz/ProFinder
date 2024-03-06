package com.vzkz.profinder.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.bottombar.MyBottomBarScaffold
import com.vzkz.profinder.ui.components.dialogs.MyAlertDialog
import com.vzkz.profinder.ui.home.components.HomeCard
import com.vzkz.profinder.ui.home.components.HomeFavList
import com.vzkz.profinder.ui.home.components.shimmer.FavListShimmer
import com.vzkz.profinder.ui.home.components.shimmer.HomeCardShimmer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, homeViewModel: HomeViewModel = hiltViewModel()) {
    homeViewModel.onInit()
    val error = homeViewModel.state.error
    var favList: List<ActorModel> by remember { mutableStateOf(emptyList()) }
    favList = homeViewModel.state.favList
    var loading by remember { mutableStateOf(true) }
    loading = homeViewModel.state.loading
    ScreenBody(
        favList = favList,
        error = error,
        loading = loading,
        onDeleteFav = { homeViewModel.onDeleteFav(it) },
        onCloseDialog = { homeViewModel.onCloseDialog() },
        onBottomBarClicked = { navigator.navigate(it) }
    )
}

@Composable
private fun ScreenBody(
    favList: List<ActorModel>,
    error: UiError,
    loading: Boolean,
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
        val cardPadding = PaddingValues(8.dp)
        val contentPadding = PaddingValues(8.dp)
        var editFavList by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            MyColumn {
                if (loading) {
                    HomeCardShimmer(
                        modifier = Modifier
                            .weight(1f),
                        cardColor = cardColor,
                        cardPadding = cardPadding,
                        contentPadding = PaddingValues(12.dp),
                        placeRight = false,
                        content = { }
                    )
                } else {
                    HomeCard( //Hired Services
                        modifier = Modifier
                            .weight(1f),
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
                }

                if (loading) {
                    HomeCardShimmer(
                        modifier = Modifier
                            .weight(1f),
                        cardColor = cardColor,
                        cardPadding = cardPadding,
                        contentPadding = contentPadding,
                        placeRight = true,
                        content = { FavListShimmer() }
                    )
                } else {
                    HomeCard( //Favorites
                        modifier = Modifier
                            .weight(1f),
                        boxModifier = Modifier,
                        cardColor = cardColor,
                        contentColor = contentColor,
                        fontFamily = fontFamily,
                        cardPadding = cardPadding,
                        contentPadding = contentPadding,
                        isEditFavListEmpty = favList.isEmpty(),
                        editFavList = editFavList,
                        onEditFavList = { editFavList = !editFavList },
                        title = "Favorites",
                        placeRight = true,
                        content = {
                            HomeFavList(
                                favList,
                                contentColor,
                                editFavList,
                                onDeleteFav
                            )
                        }
                    )
                }

                if (loading) {
                    HomeCardShimmer(
                        modifier = Modifier
                            .weight(1f),
                        cardColor = cardColor,
                        cardPadding = cardPadding,
                        contentPadding = PaddingValues(12.dp),
                        placeRight = false,
                        content = { }
                    )
                } else {
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
//            favList = emptyList(),
            favList = PROFFESIONALLISTFORTEST,
            error = UiError(false, "Account wasn't created"),
            loading = true,
//            loading = false,
            onDeleteFav = {},
            onCloseDialog = {},
            onBottomBarClicked = {},
        )
    }

}
