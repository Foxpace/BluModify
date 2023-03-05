package com.tomasrepcik.blumodify.storage.cache

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object SettingsSerializer : Serializer<AppSettings> {
    override val defaultValue: AppSettings = AppSettings.default

    override suspend fun readFrom(input: InputStream): AppSettings {
        try {
            return Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: AppSettings,
        output: OutputStream
    ) = output.write(
        Json.encodeToString(serializer = AppSettings.serializer(), value = t).toByteArray()
    )

}

val Context.settingsDataStore: DataStore<AppSettings> by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer
)