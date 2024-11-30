package com.lovinsharma.kalanikethan.screens.addscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathSegment
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lovinsharma.kalanikethan.composables.DatePickerModal
import com.lovinsharma.kalanikethan.data.room.models.Family
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentIDScreen(family: Family) {
    var paymentID by remember { mutableStateOf("") }
    var paymentDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) } // State to control dialog visibility
    var showfloatingdiaglog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = paymentID,
                onValueChange = { input -> paymentID = input },
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                ),
                placeholder = { Text("Enter paymentID") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Payment Icon"
                    )
                },
                singleLine = true,

                )

            // OutlinedTextField with leading icon for the DatePicker
            OutlinedTextField(
                value = paymentDate,
                onValueChange = { newDate -> paymentDate = newDate },
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                ),
                placeholder = { Text("Click date icon to enter payment date") },
                leadingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                readOnly = true, // Make field read-only so users can only select a date
                singleLine = true
            )

            // Show DatePickerDialog when 'showDatePicker' is true
            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { newDateMillis ->
                        newDateMillis?.let {
                            paymentDate =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(it)
                                )
                        }
                        showDatePicker = false // Hide the modal after selection
                    },
                    onDismiss = {
                        showDatePicker = false // Hide the modal if dismissed
                    }
                )
            }
        }

        Button(
            onClick = {
                family.paymentID = paymentID
                family.paymentDate = if (paymentDate == "") 0L else convertDateToLong(paymentDate)
                println(family)
                paymentID = ""
                paymentDate = ""
                showfloatingdiaglog = true
            },
            modifier = Modifier.padding(horizontal = 20.dp),
            shape = MaterialTheme.shapes.large,

        ) {
            Icon(
                imageVector =  Icons.Default.CheckCircle,
                contentDescription = "Add family",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed


            Text(
                text = "Add Family",
                style = TextStyle(
                    color = Color.White, // You can customize the text color
                    fontWeight = FontWeight.Bold, // Make the text bold
                    fontSize = 16.sp // Adjust font size
                ),

            )
        }


        if (showfloatingdiaglog) {
            FamilyAddedDialog { showfloatingdiaglog = false }
        }

    }
}

@SuppressLint("NewApi")
fun convertDateToLong(dateString: String): Long {
    // Define the date format
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Parse the date string into a LocalDate
    val localDate = LocalDate.parse(dateString, formatter)

    // Convert LocalDate to epoch milliseconds
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

@Composable
fun FamilyAddedDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = "Family has been added",
                modifier = Modifier
                    .height(50.dp).padding(10.dp)
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}
