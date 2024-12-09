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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import com.lovinsharma.kalanikethan.R
import com.lovinsharma.kalanikethan.composables.AddScreenButtons
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import org.mongodb.kbson.ObjectId

@Composable
fun HomeScreen(viewmodel: MainViewModel) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntry?.destination?.route
    val familyID = remember { mutableStateOf(ObjectId()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 0.dp, vertical = 0.dp),
    ) {
        // Box at the top for "History"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(0.dp))
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 30.dp),
            contentAlignment = Alignment.CenterStart
        ) {


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
    Text(text = "This is the family $familyID")

}




