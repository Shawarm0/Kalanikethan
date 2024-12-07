package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovinsharma.kalanikethan.models.Student
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentBox(
    student: Student,
    students: MutableList<Student>,
    onStudentChange: (Student) -> Unit
) {
    var studentName by remember { mutableStateOf(student.studentName) }
    var studentNumber by remember { mutableStateOf(student.studentNumber) }
    var additionalInfo by remember { mutableStateOf(student.additionalInfo) }
    var canWalkAlone by remember { mutableStateOf(student.canWalkAlone) }
    var studentDOB by remember { mutableStateOf(student.birthdate) }
    var showAdditionalInfo by remember { mutableStateOf(false) }
    var showAdditionalInfoButton by remember { mutableStateOf(true) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Update the Student object when any of its fields change
    LaunchedEffect(studentName, studentNumber, additionalInfo, canWalkAlone, studentDOB) {
        onStudentChange(
            Student(
                studentName = studentName,
                studentNumber = studentNumber,
                additionalInfo = additionalInfo,
                canWalkAlone = canWalkAlone,
                birthdate = studentDOB,
                familyIDfk = 0,
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
                    value = studentName,
                    onValueChange = { input ->
                        // Capitalize each word in the student name
                        studentName = input.split(" ")
                            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase(Locale.ROOT) } }
                    },
                    label = { Text("Student Name") },
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
                    value = studentNumber,
                    onValueChange = { studentNumber = it },
                    label = { Text("Student Number") },
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
                            contentDescription = "Call icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    singleLine = true
                )
            }

            // Row with Date of Birth and Can Walk Alone
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = studentDOB,
                    onValueChange = { studentDOB = it },
                    label = { Text("Click icon to set dob") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    ),
                    leadingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    readOnly = true,
                    singleLine = true
                )

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { newDateMillis ->
                            newDateMillis?.let {
                                studentDOB =
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                        Date(it)
                                    )
                            }
                            showDatePicker = false // Hide the modal after selection
                        },
                        onDismiss = {
                            showDatePicker = false // Hide the modal if dismissed
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = canWalkAlone,
                        onCheckedChange = { canWalkAlone = it },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Color.White,
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = if (isSystemInDarkTheme()) Color.Gray else Color.Black,
                        )
                    )
                    Text(
                        text = "Can walk alone",
                        style = MaterialTheme.typography.bodyLarge
                    )



                }

                if (showAdditionalInfoButton) {
                    Button(
                        onClick = {
                            showAdditionalInfo = true
                            showAdditionalInfoButton = false
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),

                        ) {
                        Icon(
                            imageVector =  Icons.Default.Info,
                            contentDescription = "Show Additional Info",
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed


                        Text(
                            text = "Show Additional Info",
                            style = TextStyle(
                                color = Color.White, // You can customize the text color
                                fontWeight = FontWeight.Bold, // Make the text bold
                                fontSize = 18.sp // Adjust font size
                            ),

                            )
                    }
                } else {
                    Button(
                        onClick = {
                            showAdditionalInfo = false
                            showAdditionalInfoButton = true
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),

                        ) {
                        Icon(
                            imageVector =  Icons.Default.Info,
                            contentDescription = "Show Additional Info",
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed


                        Text(
                            text = "Hide Additional Info",
                            style = TextStyle(
                                color = Color.White, // You can customize the text color
                                fontWeight = FontWeight.Bold, // Make the text bold
                                fontSize = 18.sp // Adjust font size
                            ),

                            )
                    }



                }

                Button(
                    onClick = {
                        students.remove(student)
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                    ),

                    ) {
                    Icon(
                        imageVector =  Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed


                    Text(
                        text = "Remove",
                        style = TextStyle(
                            color = Color.White, // You can customize the text color
                            fontWeight = FontWeight.Bold, // Make the text bold
                            fontSize = 18.sp // Adjust font size
                        ),

                        )
                }


            }

            if (showAdditionalInfo) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = additionalInfo,
                        onValueChange = { additionalInfo = it },
                        label = { Text("Additional Info") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                            focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            focusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            focusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Info Icon"
                            )
                        },
                    )


                }
            }


        }
    }




//    // Individual composables
//    TextField(
//        value = studentName,
//        onValueChange = { studentName = it },
//        label = { Text("Student Name") }
//    )
//
//    Spacer(modifier = Modifier.height(8.dp)) // Optional spacing
//
//    TextField(
//        value = studentNumber,
//        onValueChange = { studentNumber = it },
//        label = { Text("Student Number") }
//    )
//
//    Spacer(modifier = Modifier.height(8.dp)) // Optional spacing
//
//    TextField(
//        value = studentDOB,
//        onValueChange = { studentDOB = it },
//        label = { Text("Date of Birth") }
//    )
//
//    Spacer(modifier = Modifier.height(8.dp)) // Optional spacing
//
//    Row {
//        Checkbox(
//            checked = canWalkAlone,
//            onCheckedChange = { canWalkAlone = it }
//        )
//        Text("Can walk alone")
//    }
}
