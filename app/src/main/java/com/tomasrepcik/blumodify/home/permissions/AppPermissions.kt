package com.tomasrepcik.blumodify.home.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

object AppPermissions {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val permissionsSdkT = arrayOf(
        Manifest.permission_group.NEARBY_DEVICES,
        Manifest.permission_group.NOTIFICATIONS,
    )

    @RequiresApi(Build.VERSION_CODES.S)
    val permissionsSdkS = arrayOf(
        Manifest.permission_group.NEARBY_DEVICES,
    )

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun openSettings(context: Context) {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
            it.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(it)
        }
    }
}