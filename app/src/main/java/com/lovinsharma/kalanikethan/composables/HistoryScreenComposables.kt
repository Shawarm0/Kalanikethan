package com.lovinsharma.kalanikethan.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovinsharma.kalanikethan.models.SignInEvent
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventsForDayScreen(viewModel: MainViewModel, day: String) {
    // Get the list of events for the given day
    val events by remember(day) {
        mutableStateOf(viewModel.getEventsForDay(day))
    }
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black


    // Sort the events:
    val sortedEvents = events.sortedWith(
        compareByDescending<SignInEvent> { it.signOut != null } // Prioritize events with signOut = null
            .thenByDescending { it.signOut ?: Long.MIN_VALUE } // Sort by signOut time in descending order
            .thenByDescending { it.signIn } // Sort by signIn time if signOut is null
    )

    Column {
        // Iterate over sorted events
        sortedEvents.forEach { event ->
        EventBox(event, textColor)
        }
    }
}


@Composable
fun EventBox(event: SignInEvent, textColor: Color) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp, vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Student Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            event.student?.studentName?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Signed In Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Signed In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(event.signIn)),
                fontSize = 16.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Signed Out Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Signed Out",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = if (event.signOut != null) {
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(event.signOut!!))
                } else {
                    "Not Signed Out"
                },
                fontSize = 16.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        var length = ""

        // Ensure signOut is not null and calculate the time difference
        if (event.signOut != null) {
            // Calculate time difference in milliseconds
            val timeDifference = event.signOut!! - event.signIn

            // Convert milliseconds to hours and minutes
            val hours = (timeDifference / 1000) / 3600  // 3600 seconds in an hour
            val minutes = (timeDifference / 1000 % 3600) / 60  // Remaining minutes after hours are removed

            // Format the time into "HH:mm" using Locale.getDefault()
            length = String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
        }
        // Signed Out Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Length of Session",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = length,
                fontSize = 16.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }

}