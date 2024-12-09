package com.lovinsharma.kalanikethan.screens.homescreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lovinsharma.kalanikethan.composables.FamilyBox
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel

@Composable
fun AllFamilies(viewModel: MainViewModel, onEdit: (Family) -> Unit ) {
    val families by viewModel.getAllFamilies().collectAsState(initial = emptyList())
    LazyColumn {
        items(families) { family ->
            FamilyBox(family = family, onEdit = { onEdit(family) })
        }
    }
}

