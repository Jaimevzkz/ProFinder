package com.vzkz.profinder.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.profinder.core.boilerplate.SERVICEMODEL1FORTEST
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyRow
import com.vzkz.profinder.ui.components.ProfilePicture
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun ServiceDetailsDialog(
    modifier: Modifier = Modifier,
    service: ServiceModel,
    backgroundColor: Color,
    fontColor: Color
) {
    Box(
        modifier = modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.large)
            .fillMaxWidth()
            .height(500.dp)
            .background(backgroundColor),
    ) {
        MyRow(modifier = Modifier.padding(8.dp)) {
            MyColumn(modifier = Modifier) {
                ProfilePicture(profilePhoto = service.owner.profilePhoto, size = 90)
                Text(text = service.owner.nickname, fontSize = 14.sp)
                OutlinedButton(
                    modifier = Modifier.size(92.dp, 36.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "See profile", fontSize = 9.sp)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = service.name,
                modifier = Modifier,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(3f))
        }
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { /*TODO*/ }
        ) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close description")
        }


    }
}


@Composable
@Preview(showSystemUi = true)
private fun ServiceCardPreview() {
    ProFinderTheme {
        ServiceDetailsDialog(
            service = SERVICEMODEL1FORTEST,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            fontColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}