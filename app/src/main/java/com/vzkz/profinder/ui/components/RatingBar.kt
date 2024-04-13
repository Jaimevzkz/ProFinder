package com.vzkz.profinder.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vzkz.profinder.ui.theme.orange
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ClickableRatingBar(
    modifier: Modifier = Modifier,
    stars: Int = 5,
    rating: Int,
    onRatingSet: (Int) -> Unit,
    starsColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(modifier = modifier) {
        repeat(stars) { starNumber ->
            Icon(
                imageVector = if (rating > starNumber) Icons.Outlined.Star else Icons.Outlined.StarOutline,
                contentDescription = "rating",
                tint = starsColor,
                modifier = Modifier.clickable { onRatingSet(starNumber + 1) }
            )
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starSize: Int = 24,
    starsColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starSize.dp)
            )
    }
        if (halfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.StarHalf,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starSize.dp)
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}

@Composable
fun starColor(rating: Double): Color{
    return if(rating == 0.0) MaterialTheme.colorScheme.onBackground
    else if (rating < 2) Color.Red
    else if (rating < 2.5) orange
    else if (rating <= 4) Color.Yellow
    else if (rating <= 5) Color.Green
    else MaterialTheme.colorScheme.onBackground
}