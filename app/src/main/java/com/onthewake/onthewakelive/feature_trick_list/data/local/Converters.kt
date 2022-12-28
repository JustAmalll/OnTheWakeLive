package com.onthewake.onthewakelive.feature_trick_list.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.onthewake.onthewakelive.core.util.JsonParser
import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickItem

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromTrickItemJson(json: String): List<TrickItem> {
        return jsonParser.fromJson<ArrayList<TrickItem>>(
            json, object : TypeToken<ArrayList<TrickItem>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toTrickItemsJson(meanings: List<TrickItem>): String {
        return jsonParser.toJson(
            meanings, object : TypeToken<ArrayList<TrickItem>>() {}.type
        ) ?: "[]"
    }
}