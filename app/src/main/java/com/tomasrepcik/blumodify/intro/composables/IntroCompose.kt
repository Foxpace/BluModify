package com.tomasrepcik.blumodify.intro.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.components.AppButton
import com.tomasrepcik.blumodify.ui.components.BackButton
import com.tomasrepcik.blumodify.ui.components.OnClickFunction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroCompose(
    navController: NavController,
    @DrawableRes image: Int,
    @StringRes imageDescription: Int,
    @StringRes textTitle: Int,
    @StringRes textDescription: Int,
    @StringRes buttonText: Int = R.string.next,
    onNext: OnClickFunction

) = Scaffold(topBar = {
    TopAppBar(title = {}, navigationIcon = {
        BackButton(iconDescription = R.string.ic_arrow_back) {
            navController.popBackStack()
        }
    })
}) {
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
            painter = painterResource(id = image),
            contentDescription = stringResource(id = imageDescription),
            contentScale = ContentScale.Fit,
            modifier = Modifier.weight(2f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            stringResource(id = textTitle),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(id = textDescription),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            modifier = Modifier.padding(bottom = 30.dp),
            text = buttonText,
            onClick = onNext
        )
    }
}

