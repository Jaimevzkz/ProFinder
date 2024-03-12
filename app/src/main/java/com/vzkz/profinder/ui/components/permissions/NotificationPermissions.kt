package com.vzkz.profinder.ui.components.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vzkz.profinder.R

@Composable
fun PermissionDialog(
    onRequestPermission: () -> Unit
) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier,
            title = { Text("Notification permission") },
            text = { Text("Grant notification permissions so that you don't miss anything!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showWarningDialog = false
                        onRequestPermission()
                    },
                    modifier = Modifier
                ) { Text(text = "Request permissions") }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Composable
fun RationaleDialog() {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier,
            title = { Text("Notification permission") },
            text = { Text("Notification permissions are important in this app. navigate to your device settings to grant them.") },
            confirmButton = {
                TextButton(
                    onClick = { showWarningDialog = false },
                    modifier = Modifier
                ) { Text(text = stringResource(R.string.ok)) }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}