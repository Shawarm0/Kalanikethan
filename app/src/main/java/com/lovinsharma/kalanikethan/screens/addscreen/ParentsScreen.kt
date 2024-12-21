package com.lovinsharma.kalanikethan.screens.addscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovinsharma.kalanikethan.composables.ParentsBox
import com.lovinsharma.kalanikethan.models.ParentUI

@Composable
fun ParentsScreen(parents: MutableList<ParentUI>, familyName: String) {
    // Creates a scrollable box
    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            for (index in parents.indices) {
                ParentsBox(
                    parent = parents[index],
                    onParentChange = { updatedParent ->
                        parents[index] = updatedParent
                    },
                    onParentRemove = {
                        parents.remove(it)
                    }
                )
            }

        }

        // Floating Action Button positioned at the bottom right
        ExtendedFloatingActionButton(
            onClick = {
                if (familyName.isNotEmpty()) {
                    // This will carry out an action every time the Add Parent button is pressed
                    parents.add(
                        ParentUI(
                            parentName = "",
                            parentNumber = "",
                        )
                    )
                } else {
                    println("familyName cannot be empty")
                }
            },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Parent") },
            text = { Text(text = "Add Parent") },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp) // Padding applied only to FAB
        )
    }
}
