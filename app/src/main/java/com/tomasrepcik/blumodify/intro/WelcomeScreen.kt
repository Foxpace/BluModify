package com.tomasrepcik.blumodify.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.theme.AppTheme
import com.tomasrepcik.blumodify.ui.previews.BrightScreens
import com.tomasrepcik.blumodify.ui.previews.DarkScreens

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Image(
            painter = painterResource(id = R.drawable.bluetooth_icon),
            contentDescription = stringResource(id = R.string.welcome_image_description),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.weight(0.3f))
        Text(
            stringResource(id = R.string.welcome_title),
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            stringResource(id = R.string.welcome_text),
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Button(onClick = {
            navController.navigate(IntroNav.INTRO_MOTIVATION_SCREEN)
        }) {
            Text(
                stringResource(id = R.string.welcome_button),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}


@BrightScreens
@DarkScreens
@Composable
fun WelcomeScreenPreview() {
    AppTheme {
        Surface {
            val navController = rememberNavController()
            WelcomeScreen(navController = navController)
        }
    }
}