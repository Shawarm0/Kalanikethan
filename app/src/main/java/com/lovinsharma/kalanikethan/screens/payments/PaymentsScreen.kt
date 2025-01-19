package com.lovinsharma.kalanikethan.screens.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import com.lovinsharma.kalanikethan.composables.Confirmpayment
import com.lovinsharma.kalanikethan.composables.IncorrectPayment
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun sendEmail(senderEmail: String, senderPassword: String, recipientEmail: String, subject: String, body: String) {
    val properties = Properties()
    properties["mail.smtp.host"] = "smtp.gmail.com"
    properties["mail.smtp.port"] = "587"
    properties["mail.smtp.auth"] = "true"
    properties["mail.smtp.starttls.enable"] = "true"  // Use TLS
    properties["mail.smtp.ssl.trust"] = "smtp.gmail.com"

    val session = Session.getInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(senderEmail, senderPassword)
        }
    })

    val emailMessage = MimeMessage(session)
    emailMessage.setFrom(InternetAddress(senderEmail))
    emailMessage.setRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
    emailMessage.subject = subject
    emailMessage.setText(body)

    // Send email asynchronously
    AsyncTask.execute {
        try {
            Transport.send(emailMessage)
            Log.d("Email", "Email sent successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Email", "Failed to send email")
        }
    }
}

fun formatPaymentDateWithNextMonth(familyPaymentDate: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = familyPaymentDate
    // Add 1 to the month (months are 0-indexed in Calendar)
    calendar.add(Calendar.MONTH, 1)

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(calendar.time)
}


// Add and change these
@RequiresApi(Build.VERSION_CODES.O)
fun sendConfirmEmail(family: Family) {
    val senderEmail = "kalanikethansunderlandcic@gmail.com"
    val senderPassword = "npyc wybv bzpw asfr " // Use App Password or OAuth for security
    val recipientEmail = family.familyEmail
    val subject = "Payment Confirmation for Kalanikethan"
    val body =  """
    Hello,
    
    We’re pleased to confirm that we have received your payment for Kalanikethan.

    Details of Your Payment:
    - Payment Amount: £${family.paymentAmount}
    - Payment ID: ${family.paymentID}
    - Confirmation Date: ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}
    - Next Payment Due Date: ${formatPaymentDateWithNextMonth(family.paymentDate)}

    Your payment history has been updated in our records. If you have any questions or concerns about your payment, feel free to reply to this email!

    Best regards,
    Kalanikethan CIC
""".trimIndent()


    if (recipientEmail != null) {
        sendEmail(senderEmail, senderPassword, recipientEmail, subject, body)
    }

}


fun sendReminderEmail(family: Family) {
    val senderEmail = "kalanikethansunderlandcic@gmail.com"
    val senderPassword = "npyc wybv bzpw asfr " // Use App Password or OAuth for security
    val recipientEmail = family.familyEmail
    val subject = "Payment Reminder for Kalanikethan"
    val body =  """
    Hello,
    
    We would like to inform you that we have not yet recieved your payment for Kalanikethan.

    Details of Your Payment:
    - Payment Amount: £${family.paymentAmount}
    - PaymentID: ${family.paymentID}
    - Payment Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(family.paymentDate)}

    Please ensure that you pay this as soon as possible. If you have any questions or concerns about your payment, feel free to reply to this email!

    Best regards,
    Kalanikethan CIC
""".trimIndent()

    if (recipientEmail != null) {
        sendEmail(senderEmail, senderPassword, recipientEmail, subject, body)
    }
}

fun sendIncorrectEmail(family: Family, inputAmount: String) {
    val senderEmail = "kalanikethansunderlandcic@gmail.com"
    val senderPassword = "npyc wybv bzpw asfr " // Use App Password or OAuth for security
    val recipientEmail = family.familyEmail
    val subject = "Incorrect payment for Kalanikethan"
    val body =  """
    Hello,
    
    We would like to inform you that we recieved the incorrect amount for your payment regarding Kalanikethan.

    Details of Your Payment:
    - Payment Amount: £${family.paymentAmount}
    - Payment Recieved £${inputAmount}
    - PaymentID: ${family.paymentID}
    - Payment Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(family.paymentDate)}

    Please ensure that you pay the remaining amount as soon as possible. If you have any questions or concerns about your payment, feel free to reply to this email!

    Best regards,
    Kalanikethan CIC
""".trimIndent()

    if (recipientEmail != null) {
        sendEmail(senderEmail, senderPassword, recipientEmail, subject, body)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentsScreen(viewModel: MainViewModel, navController: NavHostController) {
    val familiesWithDuePayments = viewModel.paymentsFlow.collectAsState(initial = emptyList())

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
            Text(
                text = "Payments Screen",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Right
            )
        }


        for (family in familiesWithDuePayments.value) {
            FamilyBox2(family, viewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))



    }

}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FamilyBox2(family: Family, viewModel: MainViewModel) {

    val textColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
    val shadeColor = if (isSystemInDarkTheme()) Color.Gray.copy(alpha = 0.4f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
    val shadeColor2 = if (isSystemInDarkTheme()) Color.Gray.copy(alpha = 0.1f) else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
    var confirmpayment by remember { mutableStateOf(false) }
    var sendReminder by remember { mutableStateOf(false) }
    var incorrectAmountPaid by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp), // Padding for content
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Family Name and Payment ID
                Text(
                    text = "${family.familyName}: ${family.paymentID}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = textColor
                )

                // Payment Date with the same size as Family Name
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(family.paymentDate),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = textColor
                )
            }

            // Monthly Amount Label
            Text(
                text = "£${family.paymentAmount}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )

//            // Action Buttons
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                // View History Button with a fixed width
//                Button(
//                    onClick = { },
//                    modifier = Modifier.fillMaxWidth(), // Fixed width for View History button
//                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//                    shape = MaterialTheme.shapes.medium
//                ) {
//                    Text(
//                        text = "View History",
//                        fontSize = 16.sp,
//                        textAlign = TextAlign.Center,
//                        color = Color.White
//                    )
//                }
//            }

            // Buttons for Confirm Payment, Send Reminder, and Incorrect Payment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Confirm Payment Button
                Button(
                    onClick = {
                        confirmpayment = true
                              },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Confirm Payment",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

                // Send Reminder Button
                Button(
                    onClick = {
                        sendReminder = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC926)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Send Reminder",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
                // Incorrect Amount Paid Button
                Button(
                    onClick = { incorrectAmountPaid = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(254, 100, 100)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Incorrect Amount Paid",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }


            if (confirmpayment) {
                Confirmpayment(
                    onDismissRequest = {confirmpayment = false},
                    onConfirmation = {
                                        viewModel.confirmPurchaseButton(family._id)
                                        sendConfirmEmail(family)
                                        confirmpayment = false},
                    dialogTitle = "Confirm Payment",
                    dialogText = "Are you sure you want to confirm this payment?\n" +
                            "Amount: £${family.paymentAmount}\n" +
                            "Reference: ${family.paymentID}",
                    icon = Icons.Default.CheckCircle
                )
            }

            if (sendReminder) {
                Confirmpayment(
                    onDismissRequest = {sendReminder = false},
                    onConfirmation = {
                        viewModel.sendReminderButton(family._id)
                        sendReminderEmail(family)
                        sendReminder = false

                    },
                    dialogTitle = "Send Reminder",
                    dialogText = "Are you sure you want to send a reminder to\n" +
                            "Email: ${family.familyEmail}",
                    icon = Icons.Default.Info
                )
            }

            if (incorrectAmountPaid)  {
                IncorrectPayment(
                    onDismissRequest = {incorrectAmountPaid = false},
                    onConfirmation = { inputamount ->
                        viewModel.IncorrectAmountPaidButton(family._id)
                        sendIncorrectEmail(family, inputamount)
                        incorrectAmountPaid = false
                    },
                    dialogTitle = "Incorrect Amount Paid",
                    dialogText = "Are you sure the amount paid is incorrect, the paid amount should be £${family.paymentAmount}\n\n" +
                            "Please enter how much they have paid. ",
                    icon = Icons.Default.Warning
                )

            }



        }
    }
}


