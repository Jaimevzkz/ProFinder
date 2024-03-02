package com.vzkz.profinder.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.components.ProfilePicture

@Composable
fun HomeFavList(
    favList: List<ActorModel>,
    contentColor: Color,
    editFavList: Boolean,
    onDeleteFav: (String) -> Unit
) {
    var editFavList1 = editFavList
    MyColumn {
        if (favList.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.you_don_t_have_any_favorites_yet),
                color = contentColor
            )
            Spacer(modifier = Modifier.weight(1.5f))
        } else {
            LazyColumn {
                items(favList) { actor ->
                    MyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ProfilePicture(
                            profilePhoto = actor.profilePhoto,
                            size = 50
                        )
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
                        if (editFavList1) {
                            IconButton(onClick = {
                                onDeleteFav(actor.uid)
                                editFavList1 = false
                            }) {
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
    }
}