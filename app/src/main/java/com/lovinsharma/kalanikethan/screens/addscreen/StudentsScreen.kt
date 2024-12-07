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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovinsharma.kalanikethan.composables.StudentBox
import com.lovinsharma.kalanikethan.models.Student

@Composable
fun StudentsScreen(students: MutableList<Student>, familyName: String) {
    // Creates a scrollable box
    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            for (index in students.indices) {
                StudentBox(student = students[index], students = students, onStudentChange = { updatedStudent ->
                    students[index] = updatedStudent
                })
            }
        }

        // Floating Action Button positioned at the bottom right
        ExtendedFloatingActionButton(
            onClick = {
                if (familyName.isNotEmpty()) {
                    students.add(
                        Student(
                            studentName = "",
                            studentNumber = "",
                            birthdate = "",
                            additionalInfo = "",
                            canWalkAlone = false,
                            signedIn = false,
                            familyIDfk = 0
                        )
                    )
                } else {
                    println("You must add a family Name first")
                }
            },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Student") },
            text = { Text(text = "Add Student") },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp) // Add padding only to the FAB, not the scrollable content
        )
    }
}
