package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun Confirmpayment(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncorrectPayment(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit, // Pass the entered amount
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    var inputAmount by remember { mutableStateOf("") } // State to hold the entered amount

    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = "Example Icon",
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        },
        title = {
            Text(
                text = dialogTitle,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        },
        text = {
            Column {
                Text(
                    text = dialogText,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputAmount,
                    onValueChange = { inputAmount = it },
                    label = { Text("Enter Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(inputAmount) // Pass the input amount to the confirmation callback
                }
            ) {
                Text(
                    "Confirm",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    "Dismiss",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }
    )
}
