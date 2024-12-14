package com.lovinsharma.kalanikethan.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lovinsharma.kalanikethan.R
import com.lovinsharma.kalanikethan.composables.AddScreenButtons
import com.lovinsharma.kalanikethan.composables.DatePickerModal
import com.lovinsharma.kalanikethan.composables.StudentBox
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.models.FamilyUI
import com.lovinsharma.kalanikethan.models.Parent
import com.lovinsharma.kalanikethan.models.ParentUI
import com.lovinsharma.kalanikethan.models.StudentUI
import com.lovinsharma.kalanikethan.screens.addscreen.convertDateToLong
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import org.mongodb.kbson.ObjectId
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(viewmodel: MainViewModel) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val familyID = remember { mutableStateOf(ObjectId()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 0.dp, vertical = 0.dp),
    ) {
        // Box at the top for "History"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Home",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically).padding(start=16.dp),
                textAlign = TextAlign.Right
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AddScreenButtons(iconResId = R.drawable.parentss,
                    text = "Families",
                    isSelected = currentDestination == "Families",
                    onClick = { navController.navigate("Families") })

                Spacer(modifier = Modifier.width(8.dp)) // Space between buttons
                AddScreenButtons(iconResId = R.drawable.school,
                    text = "All Pupils",
                    isSelected = currentDestination == "All Students",
                    onClick = { navController.navigate("All Students") })

            }

        }

        Spacer(modifier = Modifier.height(10.dp))


        NavHost(navController = navController, startDestination = "Families") {
            composable("Families") {
                AllFamilies(viewModel = viewmodel, navController, familyID = familyID)
            }
            composable("All Students") {
                AllStudents(viewmodel)
            }
            composable("Edit") { EditFamily(viewmodel, familyID = familyID, navController) }
        }








    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFamily(viewModel: MainViewModel, familyID: MutableState<ObjectId>, navcontroller: NavController) {
    val family = viewModel.getFamilyById(familyID.value)
    if (family != null) {
        val students = remember { mutableStateListOf<StudentUI>() }
        val parents = remember { mutableStateListOf<ParentUI>() }
        var showDatePicker by remember { mutableStateOf(false) } // State to control dialog visibility
        var expanded by remember { mutableStateOf(false) }
        var showInfo by remember { mutableStateOf("Students") }

        val formattedDate: String = if (family.paymentDate == 0L)
            ""
        else
            SimpleDateFormat("dd/MM/yyyy").format(Date(family.paymentDate))

        var formmattedDate = remember { mutableStateOf(formattedDate) }

        // Populate family UI state
        val familyui = remember {
            mutableStateOf(
                FamilyUI(
                    familyName = family.familyName,
                    familyEmail = family.familyEmail ?: "",
                    paymentDate = family.paymentDate,
                    paymentID = family.paymentID,
                )
            )
        }

        // Populate lists only once when `family` changes
        LaunchedEffect(familyID.value) {
            students.clear()
            students.addAll(
                family.students.map { student ->
                    StudentUI(
                        _id = student._id,
                        studentName = student.studentName,
                        studentNumber = student.studentNumber,
                        birthdate = student.birthdate,
                        additionalInfo = student.additionalInfo ?: "",
                        canWalkAlone = student.canWalkAlone,
                    )
                }
            )
            parents.clear()
            parents.addAll(
                family.parents.map { parent ->
                    ParentUI(
                        _id = parent._id,
                        parentName = parent.parentName,
                        parentNumber = parent.parentNumber,
                    )
                }
            )
        }




        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(16.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    // Family name and Payment ID Row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        OutlinedTextField(
                            value = familyui.value.familyName,
                            onValueChange = { newValue ->
                                familyui.value = familyui.value.copy(familyName = newValue)
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                                focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                            ),
                            label = { Text("Family Name") },
                            placeholder = { Text("Enter family name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Family Icon"
                                )
                            },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = familyui.value.paymentID,
                            onValueChange = { newValue ->
                                familyui.value = familyui.value.copy(paymentID = newValue)
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                                focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                            ),
                            label = { Text("Payment ID") },
                            placeholder = { Text("Enter payment ID") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Payment Icon"
                                )
                            },
                            singleLine = true
                        )


                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Family email and Payment Date Row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        OutlinedTextField(
                            value = familyui.value.familyEmail,
                            onValueChange = { newValue ->
                                familyui.value = familyui.value.copy(familyEmail = newValue)
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                                focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                            ),
                            label = { Text("Family Email") },
                            placeholder = { Text("Enter family email") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email Icon"
                                )
                            },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = formmattedDate.value,
                            onValueChange = { newDate ->
                                familyui.value =
                                    familyui.value.copy(paymentDate = convertDateToLong(newDate))
                            },
                            modifier = Modifier
                                .weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                                focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                            ),
                            label = { Text("Payment Date") },
                            placeholder = { Text("Click date icon to enter payment date") },
                            leadingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Select date"
                                    )
                                }
                            },
                            readOnly = true, // Make field read-only so users can only select a date
                            singleLine = true
                        )


                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            onClick = {
                            },
                            modifier = Modifier.width(200.dp).padding(10.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Family",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete Family", color = Color.White)
                        }
                    }


                }
            }

            // Show DatePickerDialog when 'showDatePicker' is true
            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { newDateMillis ->
                        newDateMillis?.let {
                            formmattedDate.value =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(it)
                                )
                            familyui.value = familyui.value.copy(paymentDate = it)
                        }
                        showDatePicker = false // Hide the modal after selection
                    },
                    onDismiss = {
                        showDatePicker = false // Hide the modal if dismissed
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(16.dp)
                    )
            ) {

                Text(
                    modifier = Modifier.align(Alignment.TopStart).padding(horizontal = 20.dp, vertical = 10.dp),
                    text = showInfo,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold, // Makes the text bold
                        fontSize = 20.sp, // Adjust font size as needed
                        color = Color.Black // Sets text color to black
                    )
                )


                // Main layout
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Scrollable content
                    Column(
                        modifier = Modifier
                            .weight(1f) // Make the scrollable content take up available space
                            .verticalScroll(rememberScrollState())
                            .padding(top = 50.dp) // Space for the IconButton
                    ) {
                        if (showInfo == "Students") {
                            for (index in students.indices) {
                                StudentBox2(
                                    student = students[index],
                                    students = students,
                                    onStudentChange = { updatedStudent ->
                                        students[index] = updatedStudent
                                    }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        } else {
                            for (index in parents.indices) {
                                ParentsBox(
                                    parent = parents[index],
                                    onParentChange = { updatedParent ->
                                        parents[index] = updatedParent
                                    },
                                    onRemove = {
                                        parents.removeAt(index)
                                    }
                                )
                            }
                        }
                    }



                    // Buttons section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp).padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between buttons
                    ) {

                        Row( ) {
                            if (showInfo == "Students") {
                                // Button for adding students
                                Button(
                                    onClick = {
                                        students.add(
                                            StudentUI(
                                                _id = null,
                                                studentName = "",
                                                studentNumber = "",
                                                birthdate = "",
                                                additionalInfo = "",
                                                canWalkAlone = false,
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = Color.White
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.width(200.dp).align(Alignment.CenterVertically)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Add Student")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Add Student")
                                }
                            } else {
                                // Button for adding parents
                                Button(
                                    onClick = {
                                        parents.add(
                                            ParentUI(
                                                _id = null,
                                                parentName = "",
                                                parentNumber = "",
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = Color.White
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.width(200.dp).align(Alignment.CenterVertically)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Add Parent")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Add Parent")
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Save button
                            Button(
                                onClick = {
                                    viewModel.onSaveButtonPressed(
                                        family = familyui.value,
                                        familyId = familyID.value,
                                        parents = parents,
                                        students = students
                                    )
                                    navcontroller.navigate("Families")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF66BB6A),
                                    contentColor = Color.White
                                ),
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.width(200.dp).align(Alignment.CenterVertically)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Save")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Save")
                            }


                        }

                    }
                }



                // IconButton at the top
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Student Information",
                                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                                )
                            },
                            onClick = {
                                showInfo = "Students"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Parent Information",
                                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                                )
                            },
                            onClick = {
                                showInfo = "Parents"
                                expanded = false
                            }
                        )
                    }
                }




            }








        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentsBox(
    parent: ParentUI,
    onParentChange: (ParentUI) -> Unit,
    onRemove: () -> Unit // Callback for the Remove button
) {
    val parentId = parent._id
    var parentName by remember { mutableStateOf(parent.parentName) }
    var parentNumber by remember { mutableStateOf(parent.parentNumber) }

    // Update the Parent object when any of its fields change
    LaunchedEffect(parentName, parentNumber) {
        onParentChange(
            ParentUI(
                _id = parentId,
                parentName = parentName,
                parentNumber = parentNumber,
            )
        )
    }

    Box(
        modifier = Modifier.padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Row with Parent Name and Parent Number
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = parentName,
                    onValueChange = { input ->
                        // Capitalize each word in the parent name
                        parentName = input.split(" ")
                            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase(Locale.ROOT) } }
                    },
                    label = { Text("Parent Name") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(0.dp))

                OutlinedTextField(
                    value = parentNumber,
                    onValueChange = { parentNumber = it },
                    label = { Text("Parent Number") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
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



            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth(0.25f), // Make the button take up half the width of the column , // Optional padding for spacing
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Parent",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Remove", color = Color.White)
            }

        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentBox2(
    student: StudentUI,
    students: MutableList<StudentUI>,
    onStudentChange: (StudentUI) -> Unit
) {
    val studentID = student._id
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
            StudentUI(
                _id = studentID,
                studentName = studentName,
                studentNumber = studentNumber,
                additionalInfo = additionalInfo,
                canWalkAlone = canWalkAlone,
                birthdate = studentDOB
            )
        )
    }

    Column(
        modifier = Modifier.background(color = Color.Transparent)
            .padding(horizontal = 20.dp)// Inner padding for the content
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Student Name Field
            OutlinedTextField(
                value = studentName,
                onValueChange = { input ->
                    studentName = input.split(" ")
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase(Locale.ROOT) } }
                },
                label = { Text("Student Name") },
                singleLine = true,
                modifier = Modifier.weight(1f), // Use weight to make this field take up half the screen
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                    focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                ),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") }
            )

            Spacer(modifier = Modifier.width(30.dp)) // Spacer with width 30dp

            // Student Number Field
            OutlinedTextField(
                value = studentNumber,
                onValueChange = { studentNumber = it },
                label = { Text("Student Number") },
                singleLine = true,
                modifier = Modifier.weight(1f), // Use weight to make this field take up half the screen
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                    focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                ),
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = "Call icon") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Date of Birth Field
            OutlinedTextField(
                value = studentDOB,
                onValueChange = { studentDOB = it },
                label = { Text("Click icon to set dob") },
                modifier = Modifier,
                singleLine = true,
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                    focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                }
            )

            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { newDateMillis ->
                        newDateMillis?.let {
                            studentDOB =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                        }
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Can Walk Alone Checkbox
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = canWalkAlone,
                    onCheckedChange = { canWalkAlone = it },
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = Color.White,
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = if (isSystemInDarkTheme()) Color.Gray else Color.Black,
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Can walk alone", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Buttons
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        showAdditionalInfo = !showAdditionalInfo
                        showAdditionalInfoButton = !showAdditionalInfoButton
                    },
                    modifier = Modifier,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Additional Info",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (showAdditionalInfo) "Hide Additional Info" else "Show Additional Info")
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = { students.remove(student) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Parent",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Remove")
                }
            }


        }

        Spacer(modifier = Modifier.height(10.dp))
        // Additional Info Field
        if (showAdditionalInfo) {
            OutlinedTextField(
                value = additionalInfo ?: "",
                onValueChange = { additionalInfo = it },
                label = { Text("Additional Info") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                    focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                ),
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Info Icon") }
            )
        }
    }
}







