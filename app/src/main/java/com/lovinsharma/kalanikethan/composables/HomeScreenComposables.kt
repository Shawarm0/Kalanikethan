package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun DeleteFamilyAlert(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon", tint = if (isSystemInDarkTheme()) Color.White else Color.Black)
        },
        title = {
            Text(text = dialogTitle,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black)
        },
        text = {
            Text(text = dialogText,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            }
        }
    )
}