package com.tomasrepcik.blumodify.app.storage.room.entities

import androidx.room.TypeConverter
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun btItemListToJson(value: List<BtItem>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String): List<BtItem> = Json.decodeFromString(value)
}