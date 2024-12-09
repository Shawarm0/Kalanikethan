package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovinsharma.kalanikethan.models.Family
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun FamilyBox(family: Family, onEdit: () -> Unit) {
    val students = family.students
    val parents = family.parents

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Padding around the box
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(16.dp)) // Background using MaterialTheme
            .clip(RoundedCornerShape(16.dp)) // Rounded corners
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp)) // Light border
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp), // Padding for content
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Family Name
            Text(
                text = family.familyName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp) // Space below the family name
            )

            // Row to display students and parents
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Column for Students
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp) // Padding to separate from parents' column
                ) {
                    Text(
                        text = "Students:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // FlowRow to display students horizontally, wrapping if needed
                    FlowRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        if (students.isEmpty()) {
                            Text("No students found.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            for (student in students) {
                                Text(
                                    text = student.studentName,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                // Column for Parents
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp) // Padding to separate from students' column
                ) {
                    Text(
                        text = "Parents:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // FlowRow to display parents horizontally, wrapping if needed
                    FlowRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        if (parents.isEmpty()) {
                            Text("No parents found.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            for (parent in parents) {
                                Text(
                                    text = parent.parentName,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .background(
                                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }



                // Edit Button at the bottom
                Button(
                    onClick = { onEdit() },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(300.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Edit",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }


        }
    }
}




