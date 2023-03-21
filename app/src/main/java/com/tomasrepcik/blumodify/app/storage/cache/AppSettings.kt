package com.tomasrepcik.blumodify.app.storage.cache

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val isOnboarded: Boolean,
    val isAdvancedSettings: Boolean
) {

    companion object {
        val default = AppSettings(isOnboarded = false, isAdvancedSettings = false)
    }
}


