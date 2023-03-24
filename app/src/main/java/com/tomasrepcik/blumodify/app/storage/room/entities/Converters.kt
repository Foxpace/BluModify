package com.tomasrepcik.blumodify.app.storage.room.entities

import androidx.room.TypeConverter
import com.tomasrepcik.blumodify.settings.shared.model.BtItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun btItemListToJson(value: ArrayList<BtItem>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String): ArrayList<BtItem> = Json.decodeFromString(value)
}