package com.lovinsharma.kalanikethan.screens.homescreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.lovinsharma.kalanikethan.composables.FamilyBox
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import org.mongodb.kbson.ObjectId

@Composable
fun AllFamilies(viewModel: MainViewModel, navController: NavController, familyID: MutableState<ObjectId>) {
    val families by viewModel.getAllFamilies().collectAsState(initial = emptyList())
    LazyColumn {
        items(families) { family ->
            FamilyBox(family = family, onEdit = {
                familyID.value = family._id // Update the value, not the reference
                navController.navigate("Edit")
            })
        }
    }
}



