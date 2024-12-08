package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lovinsharma.kalanikethan.models.Parent
import com.lovinsharma.kalanikethan.models.ParentUI
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentsBox(
    parent: ParentUI,
    onParentChange: (ParentUI) -> Unit
) {
    var parentName by remember { mutableStateOf(parent.parentName) }
    var parentNumber by remember { mutableStateOf(parent.parentNumber) }

    // Update the Student object when any of its fields change
    LaunchedEffect(parentName, parentNumber) {
        onParentChange(
            ParentUI(
                parentName = parentName,
                parentNumber = parentNumber,
            )
        )
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(24.dp) // Inner padding for the content
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Row with Student Name and Student Number
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = parentName,
                    onValueChange = { input ->
                        // Capitalize each word in the student name
                        parentName = input.split(" ")
                            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase(Locale.ROOT) } }
                    },
                    label = { Text("Parent Name") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = parentNumber,
                    onValueChange = { parentNumber = it },
                    label = { Text("Parent Number") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = "Call Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    singleLine = true
                )
            }
        }


    }
}
