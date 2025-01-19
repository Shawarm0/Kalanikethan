package com.lovinsharma.kalanikethan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lovinsharma.kalanikethan.composables.StudentBox
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun WhoInScreen(viewModel: MainViewModel) {


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        val students by viewModel.signedStudents.collectAsState()
        var query by remember { mutableStateOf("") }

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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Who's In",
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
                        viewModel.updateSignedInQuery(query)
                    },
                    onClear = {
                        query = ""
                        viewModel.updateSignedInQuery("")
                    }
                )

            }
        }


        LazyColumn {
            items(students) { student ->
                StudentBox(
                    student = student,
                    onSignIn = { viewModel.signOut(student)
                               viewModel.signOutStudent(student._id)},
                    bootstring = "Sign Out"
                )
            }
        }




    }


}