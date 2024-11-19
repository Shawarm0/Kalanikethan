package com.lovinsharma.kalanikethan.screens

import android.text.Layout
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathSegment
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun AddScreen(navController: NavController) {
    var familyName by remember { mutableStateOf("") }
    var showAddButton by remember { mutableStateOf(false) }
    var addState by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = true, state = rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        // This creates the box at the top of the screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // This is the enter family name thing
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        BasicTextField(
                            value = familyName,
                            onValueChange = { input ->
                                familyName = input.split(" ")
                                    .joinToString(" ") { it.capitalize(Locale.ROOT) }
                                showAddButton =
                                    familyName.isNotBlank() // Show button if there's input
                            },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            modifier = Modifier
                                .weight(1f), // Ensures text field takes up as much width as possible
                            singleLine = true,
                            cursorBrush = SolidColor(Color.White),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words // Capitalize first letter of each word
                            ),
                        ) {
                            innerTextField ->
                            if (familyName.isEmpty()) {
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

                        if (showAddButton) {
                            if (addState) {
                                IconButtonWithText(
                                    onClick = {
                                        showAddButton = false
                                        addState = false
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
                                    onClick = {
                                        showAddButton = false
                                    },
                                    icon = {
                                        Icon(
                                            imageVector =  Icons.Default.Edit,
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


                }





            }








        }






    }


}



@Composable
fun IconButtonWithText(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(start = 8.dp)
            .width(180.dp), // Adjust the width based on content
        colors = buttonColors,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Leading Icon
            icon()

            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

            // Text
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}