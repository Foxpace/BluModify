package com.tomasrepcik.blumodify.e2e.helpers.config

import android.Manifest
import android.os.Build

sealed class TestConfig(
    val permissions: Array<String>,
) {
    data object AllPermissions : TestConfig(
        listOfNotNull(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else null,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Manifest.permission_group.NEARBY_DEVICES else null
        ).toTypedArray(),
    )

    data object NoPermissions : TestConfig(
        emptyArray()
    )
}