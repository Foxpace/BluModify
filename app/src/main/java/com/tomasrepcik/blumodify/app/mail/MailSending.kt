package com.tomasrepcik.blumodify.app.mail

import android.content.Context
import android.content.Intent
import android.os.Build
import com.tomasrepcik.blumodify.BuildConfig
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem

object MailSending {

    fun <T> reportError(context: Context, error: AppResult.Error<T>) =
        Intent(Intent.ACTION_SEND).let {
            it.type = "message/rfc822"
            it.flags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            it.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(context.getString(R.string.about_screen_mail_contact))
            )
            it.putExtra(Intent.EXTRA_SUBJECT, "Short Error description")
            it.putExtra(
                Intent.EXTRA_TEXT, "Android version: ${Build.VERSION.SDK_INT}\n" +
                        "App version: ${BuildConfig.VERSION_CODE}\n" +
                        "Phone: ${Build.MANUFACTURER} - ${Build.MODEL}\n\n" +
                        "Steps to reproduce issue or other additional information: " +
                        "" +
                        "\n\n\n$error"
            )
            context.startActivity(Intent.createChooser(it, context.getString(R.string.pick_mail)))
        }

    fun reportLog(context: Context, log: LogReportUiItem) = Intent(Intent.ACTION_SEND).let {
        it.type = "message/rfc822"
        it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        it.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(context.getString(R.string.about_screen_mail_contact))
        )
        it.putExtra(Intent.EXTRA_SUBJECT, "Short Error description")
        it.putExtra(
            Intent.EXTRA_TEXT, "Android version: ${Build.VERSION.SDK_INT}\n" +
                    "App version: ${BuildConfig.VERSION_CODE}\n" +
                    "Phone: ${Build.MANUFACTURER} - ${Build.MODEL}\n\n" +
                    "Steps to reproduce issue or other additional information: " +
                    "" +
                    "\n\n\n$log"
        )
        context.startActivity(Intent.createChooser(it, context.getString(R.string.pick_mail)))
    }
}