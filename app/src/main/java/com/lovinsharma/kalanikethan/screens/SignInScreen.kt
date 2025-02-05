package com.lovinsharma.kalanikethan.screens

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lovinsharma.kalanikethan.composables.StudentBox
import com.lovinsharma.kalanikethan.screens.homescreen.SearchBar
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SignInScreen(viewModel: MainViewModel) {
    val students by viewModel.unsignedStudents.collectAsState()
    var query by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        // Search Bar Box at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    textAlign = TextAlign.Right
                )

                SearchBar2(
                    query = query,
                    onQueryChanged = { input ->
                        query = input.split(" ")
                            .joinToString(" ") {
                                it.replaceFirstChar { char ->
                                    if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString()
                                }
                            }
                        viewModel.updateUnsignedInQuery(query)
                    },
                    onClear = {
                        query = ""
                        viewModel.updateUnsignedInQuery("")
                    }
                )
            }
        }

        // List of students
        LazyColumn {
            items(students) { student ->
                StudentBox(
                    student = student,
                    onSignIn = { student1 ->
                        viewModel.signIn(student1)
                        viewModel.signInStudent(student1._id)
                    },
                    bootstring = "Sign In"
                )
            }
        }
    }
}


fun getFormattedDay(date: Date): String {
    val dayFormat = SimpleDateFormat("d", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())

    val day = dayFormat.format(date).toInt()
    val month = monthFormat.format(date)

    // Determine the correct ordinal suffix
    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    return "$day$suffix $month"
}


@Composable
fun SearchBar2(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(400.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .height(48.dp)
            .border(color = MaterialTheme.colorScheme.primary, width = 1.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = { onQueryChanged(it) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(if (isSystemInDarkTheme()) Color.White else Color.Black),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 12.dp) // Move the inner text and cursor down slightly
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )



            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onClear() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

