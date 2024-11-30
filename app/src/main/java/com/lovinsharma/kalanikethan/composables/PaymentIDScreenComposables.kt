package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    // DatePickerDialog with confirm and dismiss buttons
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel",  color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            }
        },
    )
    {
        DatePicker(state = datePickerState,
            colors = DatePickerDefaults.colors(
                weekdayContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                dayContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                selectedYearContentColor = Color.White,
                currentYearContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                todayDateBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                todayContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                selectedYearContainerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                dateTextFieldColors = if (isSystemInDarkTheme()) TextFieldDefaults.textFieldColors(focusedTextColor = Color.White, unfocusedLabelColor = Color.White, focusedLabelColor = Color.White) else TextFieldDefaults.textFieldColors(focusedTextColor = Color.Black),
            )
        )
    }
}