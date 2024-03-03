package com.vzkz.profinder.ui.profile.shimmer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ProfileScreenShimmer(
    modifier: Modifier = Modifier,
) {

    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    MyColumn(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        //Top screen
        val iconShimmerSize = 24.dp
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( //Icon edit
                modifier = Modifier
                    .shimmer()
                    .size(iconShimmerSize)
                    .background(Color.Gray)
            )
            MySpacer(size = 16)
            Box( //Icon edit
                modifier = Modifier
                    .shimmer()
                    .size(iconShimmerSize)
                    .background(Color.Gray)
            )
        }

        //Header
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( //Profile picture
                modifier = Modifier
                    .shimmer()
                    .size(100.dp)
                    .shadow(elevation = 1.dp, shape = CircleShape)
                    .background(Color.Gray)
            )
            MySpacer(size = 4)
            val titleHeight = 35.dp
            Column(modifier = Modifier.padding(12.dp)) {
                Box( //Name
                    modifier = Modifier
                        .shimmer()
                        .height(titleHeight)
                        .width(100.dp)
                        .background(Color.Gray)
                )
                MySpacer(size = 16)
                Box( //Last name
                    modifier = Modifier
                        .shimmer()
                        .height(titleHeight)
                        .width(200.dp)
                        .background(Color.Gray)
                )
            }
        }
        //State
        MySpacer(size = 8)
        Box( //State (Prof)
            modifier = Modifier
            .shimmer()
            .height(60.dp)
            .width(180.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(40))
            .background(Color.Gray)
        )

        //profile_details
        MySpacer(size = 16)
        Column(
            modifier = Modifier
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(10))
                .background(cardColor)
                .padding(horizontal = 16.dp)
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val spaceBetween = 14
            val innerSpaceBetween = 6
            val normalTextHeight = 24.dp
            MySpacer(size = spaceBetween)
            Box( //title nickname
                modifier = Modifier
                    .shimmer()
                    .height(normalTextHeight)
                    .width(100.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = innerSpaceBetween)
            Box( //content nickname
                modifier = Modifier
                    .shimmer()
                    .height(normalTextHeight)
                    .width(70.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = spaceBetween)
            Box( //title description
                modifier = Modifier
                    .shimmer()
                    .height(normalTextHeight)
                    .width(100.dp)
                    .background(Color.Gray)
            )
            MySpacer(size = innerSpaceBetween)
            val interLine = 6
            Box( //content description
                modifier = Modifier
                    .shimmer()
                    .height(normalTextHeight)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            MySpacer(size = interLine)
            Box(
                modifier = Modifier
                    .shimmer()
                    .height(normalTextHeight)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            MySpacer(size = interLine)
            Box(
                modifier = Modifier
                    .shimmer()
                    .height(normalTextHeight)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
        }

        //Footer
        MySpacer(size = 24)
        Box( //Logout Button
            modifier = Modifier
                .shimmer()
                .height(40.dp)
                .width(100.dp)
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(60))
                .background(Color.Gray)
        )
        MySpacer(size = 24)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LightPreview() {
    ProFinderTheme {
        ProfileScreenShimmer()
    }
}