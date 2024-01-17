package com.tomasrepcik.blumodify.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.tomasrepcik.blumodify.BuildConfig
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme


@Composable
fun AboutScreen(drawerState: DrawerState) {
    Scaffold(
        modifier = Modifier.testTag(AboutTestTags.ABOUT_SCREEN),
        topBar = { AppBar(drawerState = drawerState, title = R.string.about_screen_title) }
    ) { padding ->
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_ICON),
                painter = painterResource(id = R.drawable.ic_app_empty),
                contentScale = ContentScale.Fit,
                contentDescription = stringResource(
                    id = R.string.app_name
                )
            )
            Text(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_TITLE),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_DESCRIPTION),
                text = stringResource(id = R.string.about_screen_text),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Justify),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.more_actions),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Start),
            )
            Spacer(modifier = Modifier.height(16.dp))
            AboutScreenLine(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_VERSION),
                img = R.drawable.ic_android,
                imageDescription = R.string.ic_android,
                text = stringResource(
                    id = R.string.about_screen_version,
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE
                )
            )
            AboutScreenLine(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_GITHUB),
                img = R.drawable.ic_github,
                imageDescription = R.string.ic_github,
                text = stringResource(id = R.string.about_screen_github_text)
            ) {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.about_screen_github_link))
                ).also {
                    context.startActivity(it)
                }
            }
            AboutScreenLine(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_MAIL),
                img = R.drawable.ic_mail,
                imageDescription = R.string.ic_mail,
                text = stringResource(id = R.string.about_screen_mail_text)
            ) {
                Intent(Intent.ACTION_SENDTO).let {
                    it.data = Uri.parse("mailto:${context.getString(R.string.about_screen_mail_contact)}")
                    context.startActivity(it)
                }
            }
            AboutScreenLine(
                modifier = Modifier.testTag(AboutTestTags.ABOUT_LICENSES),
                img = R.drawable.ic_license,
                imageDescription = R.string.ic_license,
                text = stringResource(id = R.string.about_screen_license_text)
            ) {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }
        }
    }
}

@AllScreenPreview
@Composable
fun AboutScreenPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    BluModifyTheme {
        AboutScreen(drawerState)
    }
}