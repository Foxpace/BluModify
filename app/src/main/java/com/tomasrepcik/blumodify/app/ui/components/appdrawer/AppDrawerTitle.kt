package com.tomasrepcik.blumodify.app.ui.components.appdrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R

@Composable
fun AppDrawerTitle() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =Alignment.CenterHorizontally,
        modifier = Modifier.height(224.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_empty),
            contentDescription = stringResource(R.string.welcome_image_description),
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )
    }
}