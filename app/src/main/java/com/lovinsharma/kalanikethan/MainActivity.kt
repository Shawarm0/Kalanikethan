package com.lovinsharma.kalanikethan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.screens.addscreen.AddScreen
import com.lovinsharma.kalanikethan.screens.HistoryScreen
import com.lovinsharma.kalanikethan.screens.homescreen.HomeScreen
import com.lovinsharma.kalanikethan.screens.PaymentsScreen
import com.lovinsharma.kalanikethan.screens.SignInScreen
import com.lovinsharma.kalanikethan.screens.WhoInScreen
import com.lovinsharma.kalanikethan.screens.homescreen.UpdateScreen
import com.lovinsharma.kalanikethan.ui.theme.KalanikethanTheme
import com.lovinsharma.kalanikethan.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            KalanikethanTheme(darkTheme = isSystemInDarkTheme()) {
                val viewModel: MainViewModel = viewModel()
                var family: Family? = null
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Row(Modifier.fillMaxSize()) {

                        // These variables handle screen switching
                        val navController = rememberNavController()
                        val navOptions = NavOptions.Builder() // This is currently not working
                            .setEnterAnim(0)      // No enter animation
                            .setExitAnim(0)       // No exit animation
                            .setPopEnterAnim(0)   // No pop enter animation
                            .setPopExitAnim(0)    // No pop exit animation
                            .build()

                        // Creating the appbar
                        Appbar(
                            modifier = Modifier
                                .padding(innerPadding)
                                .width(200.dp)
                                .fillMaxHeight(),
                            onScreenSelected = { screen ->
                                if (navController.currentBackStackEntry?.destination?.route!=screen) {
                                    navController.navigate(screen, navOptions = navOptions)
                                }
                            }
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.Top,
                        ) {
                            NavHost(navController = navController, startDestination = "Home",
                                // Disable transitions
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None },
                                ) {
                                composable("Home") { HomeScreen(viewModel, onEdit = { familyincoming ->
                                    family = familyincoming

                                    navController.navigate("UpdateScreen")}) }
                                composable("SignIn") { SignInScreen(viewModel) }
                                composable("AddScreen") { AddScreen(viewModel) }
                                composable("WhoInScreen") { WhoInScreen(navController) }
                                composable("History") { HistoryScreen(navController) }
                                composable("PaymentsScreen") { PaymentsScreen(navController) }
                                composable("UpdateScreen") { UpdateScreen(viewModel, family) }
                            }
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun CustomIcon(
    iconResId: Int,
    contentDescription: String
) {
    // Load the custom icon using painterResource
    val painter = painterResource(id = iconResId)
    val iconTint = Color.White

    // Display the icon using Icon Composable
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(0.dp)
            .size(25.dp),
        tint = iconTint
    )
}

@Composable
fun Appbar(
    modifier: Modifier = Modifier,
    onScreenSelected: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .height(500.dp)
            .width(200.dp),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // This variable is solely used for changing the colour of the button when the screen is selected
            var selectedScreen by remember { mutableStateOf("home") }


            // If the system is in dark theme then we use the dark theme logo which is purple
            if (isSystemInDarkTheme()) {
                Image(
                    painter = painterResource(id = R.drawable.logo_dark_1_), // Replace with your logo
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp)
                )
            }
            else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))


            // This is the home button
            AppBarButton(
                iconResId = R.drawable.home,
                text = " Home",
                isSelected = selectedScreen == "home",
                onClick = {
                    selectedScreen = "home"
                    onScreenSelected("Home")
                }
            )

            // This is the login sign in button
            AppBarButton(
                iconResId = R.drawable.login,
                text = " Sign In",
                isSelected = selectedScreen == "signin",
                onClick = {
                    selectedScreen = "signin"
                    onScreenSelected("SignIn")
                }
            )

            // This is the add button
            AppBarButton(
                iconResId = R.drawable.add,
                text = " Add",
                isSelected = selectedScreen == "addfamily",
                onClick = {
                    selectedScreen = "addfamily"
                    onScreenSelected("AddScreen")
                }
            )

            // This is the who's in button
            AppBarButton(
                iconResId = R.drawable.search,
                text = " Who's In",
                isSelected = selectedScreen == "whosin",
                onClick = {
                    selectedScreen = "whosin"
                    onScreenSelected("WhoInScreen")
                }
            )

            // This is the history screen
            AppBarButton(
                iconResId = R.drawable.history,
                text = " History",
                isSelected = selectedScreen == "history",
                onClick = {
                    selectedScreen = "history"
                    onScreenSelected("History")
                }
            )

            // This is the payments screen
            AppBarButton(
                iconResId = R.drawable.wallet,
                text = " Payments",
                isSelected = selectedScreen == "payments",
                onClick = {
                    selectedScreen = "payments"
                    onScreenSelected("PaymentsScreen")
                }
            )




        }
    }


}

@Composable
fun AppBarButton(iconResId: Int, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(180.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
        ),
        shape = MaterialTheme.shapes.medium // Use MaterialTheme's default button shape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Use CustomIcon composable to display the custom icon
            CustomIcon(iconResId = iconResId, contentDescription = text)

            Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed

            Text(
                text = text,
                color = Color.White, // Text color
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 8.dp) // Add padding between icon and text
            )
        }
    }
}

