package com.lovinsharma.kalanikethan.screens.homescreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lovinsharma.kalanikethan.R
import com.lovinsharma.kalanikethan.composables.AddScreenButtons
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.models.FamilyUI
import com.lovinsharma.kalanikethan.models.ParentUI
import com.lovinsharma.kalanikethan.models.StudentUI
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import org.mongodb.kbson.ObjectId
import java.text.SimpleDateFormat
import java.util.Date

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
            composable("Edit") { EditFamily(viewmodel, familyID = familyID) }
        }








    }


}

@Composable
fun EditFamily(viewmodel: MainViewModel, familyID: MutableState<ObjectId>) {
    val family = viewmodel.getFamilyById(familyID.value)
    if (family != null) {
        val students = remember { mutableStateListOf<StudentUI>() }
        val parents = remember { mutableStateListOf<ParentUI>() }

        val formattedDate: String = if (family.paymentDate == 0L)
            ""
        else
            SimpleDateFormat("dd/MM/yyyy").format(Date(family.paymentDate))

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
                        parentName = parent.parentName,
                        parentNumber = parent.parentNumber,
                    )
                }
            )
        }










    }
}






