package com.lovinsharma.kalanikethan.screens.addscreen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lovinsharma.kalanikethan.R
import com.lovinsharma.kalanikethan.composables.AddScreenButtons
import com.lovinsharma.kalanikethan.composables.IconButtonWithText
import com.lovinsharma.kalanikethan.models.FamilyUI
import com.lovinsharma.kalanikethan.models.ParentUI
import com.lovinsharma.kalanikethan.models.StudentUI
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun AddScreen(viewModel: MainViewModel) {
    val students = remember { mutableStateListOf<StudentUI>() }
    val parents = remember { mutableStateListOf<ParentUI>() }

    val family by remember {
        mutableStateOf(
            FamilyUI(
                familyName = "",
                familyEmail = "",
                paymentDate = 0L,
                paymentID = "",
                 paymentAmount = ""
            )
        )
    }
    val familyName = remember { mutableStateOf("") }

    var showAddButton by remember { mutableStateOf(false) }
    var addState = remember { mutableStateOf(true) }

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        // Top Row: Enter Family Name and Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Enter Family Name field
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp), // Add spacing between text field and buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = familyName.value,
                    onValueChange = { input ->
                        familyName.value = input.split(" ")
                            .joinToString(" ") {
                                it.replaceFirstChar { char ->
                                    if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString()
                                }
                            }
                        showAddButton = familyName.value.isNotBlank()
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.weight(1f).padding(start=16.dp),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                ) { innerTextField ->
                    if (familyName.value.isEmpty()) {
                        Text(
                            text = "Enter Family Name",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    innerTextField()
                }

                // Add/Edit Family Button
                if (showAddButton) {
                    if (addState.value) {
                        IconButtonWithText(
                            onClick = {
                                showAddButton = false
                                addState.value = false
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Add Family",
                                    tint = Color.White
                                )
                            },
                            text = "Add Family",
                            buttonColors = ButtonDefaults.buttonColors(
                                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        )
                    } else {
                        IconButtonWithText(
                            onClick = { showAddButton = false },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Family",
                                    tint = Color.White
                                )
                            },
                            text = "Edit Family",
                            buttonColors = ButtonDefaults.buttonColors(
                                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        )
                    }
                }
            }

            // Navigation Buttons
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AddScreenButtons(
                    iconResId = R.drawable.student,
                    text = "Students",
                    isSelected = currentDestination == "Students",
                    onClick = { navController.navigate("Students") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                AddScreenButtons(
                    iconResId = R.drawable.parents,
                    text = "Parents",
                    isSelected = currentDestination == "Parents",
                    onClick = { navController.navigate("Parents") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                AddScreenButtons(
                    iconResId = R.drawable.payid,
                    text = "Payment ID",
                    isSelected = currentDestination == "Payment ID",
                    onClick = { navController.navigate("Payment ID") }
                )
            }
        }

        // Navigation Screens
        NavHost(
            navController = navController, startDestination = "Students",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable("Students") {
                StudentsScreen(
                    students = students,
                    familyName = familyName.value
                )
            }
            composable("Parents") {
                ParentsScreen(
                    parents = parents,
                    familyName = familyName.value
                )
            }
            composable("Payment ID") {
                PaymentIDScreen(
                    family = family,
                    familyName,
                    parents = parents,
                    students = students,
                    addState = addState,
                    viewModel = viewModel
                )
            }
        }
    }
}




