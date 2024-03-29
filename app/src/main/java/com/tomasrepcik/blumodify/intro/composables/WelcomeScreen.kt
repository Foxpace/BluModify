package com.tomasrepcik.blumodify.intro.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.AppButton
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.intro.IntroNavOption

@Composable
fun WelcomeScreen(navController: NavController = rememberNavController()) = Scaffold(
    modifier = Modifier.testTag("WelcomeScreen")
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(it),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_app_empty),
            contentDescription = stringResource(id = R.string.welcome_image_description),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.weight(5f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            stringResource(id = R.string.welcome_title),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(id = R.string.welcome_text),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            modifier = Modifier
                .padding(bottom = 30.dp),
            text = R.string.next
        ) {
            navController.navigate(IntroNavOption.MotivationEnergyScreen.name)
        }
    }
}


@AllScreenPreview
@Composable
fun WelcomeScreenPreview() {
    BluModifyTheme {
        WelcomeScreen()
    }
}

