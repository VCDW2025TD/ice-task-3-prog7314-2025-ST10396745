package com.example.memestreamproto.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    @JvmStatic
    fun fromList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    @JvmStatic
    fun toList(value: String): List<String> =
        if (value.isBlank()) emptyList() else gson.fromJson(value, listType)
}
