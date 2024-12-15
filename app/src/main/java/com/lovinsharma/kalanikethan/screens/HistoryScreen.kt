package com.lovinsharma.kalanikethan.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lovinsharma.kalanikethan.composables.EventsForDayScreen
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel

@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        val days = viewModel.getUniqueDays()

        // Header Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "History",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Right
            )
        }

        // Day Boxes with Expandable Content
        for (day in days) {
            var isExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { isExpanded = !isExpanded }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = day,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(if (isExpanded) 180f else 0f),
                        tint = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
                    )
                }

                // Expandable Events
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                    exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                    ) {
                        EventsForDayScreen(viewModel, day)
                    }
                }
            }
        }
    }
}